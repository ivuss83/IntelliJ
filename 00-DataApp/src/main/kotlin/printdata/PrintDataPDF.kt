package printdata

import com.lowagie.text.Document
import com.lowagie.text.Element
import com.lowagie.text.Font
import com.lowagie.text.PageSize
import com.lowagie.text.Paragraph
import com.lowagie.text.Rectangle
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import dataclass.Cliente
import dataclass.Materiale
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FileOutputStream

fun generaPdf(
    cliente: Cliente,
    totaleOre: Double,
    tariffaOraria: Double,
    materialiRiepilogo: List<Pair<Materiale, Double>>
) {
    // --- 1) FILE DIALOG PRIMA DI TUTTO ---
    val fileDialog = FileDialog(null as Frame?, "Salva PDF", FileDialog.SAVE)
    fileDialog.file = "Riepilogo_${cliente.fullName.replace(" ", "_")}.pdf"
    fileDialog.isVisible = true

    val selectedFile = fileDialog.file
    val selectedDir = fileDialog.directory

    if (selectedFile == null || selectedDir == null) {
        println("Salvataggio annullato")
        return
    }

    val path = "$selectedDir$selectedFile"

    // --- 2) CREA DOCUMENTO E WRITER ---
    val documento = Document(PageSize.A4)
    PdfWriter.getInstance(documento, FileOutputStream(path))

    documento.open()

    // ----------------------------------------------------
    // INTESTAZIONE
    // ----------------------------------------------------
    val titolo = Paragraph(cliente.fullName, Font(Font.HELVETICA, 22f, Font.BOLD))
    titolo.alignment = Element.ALIGN_CENTER
    documento.add(titolo)

    documento.add(Paragraph("\n"))

    // Linea divisoria
    val line = Paragraph("______________________________________________")
    line.alignment = Element.ALIGN_CENTER
    documento.add(line)

    documento.add(Paragraph("\n"))

    // ----------------------------------------------------
    // SEZIONE ORE
    // ----------------------------------------------------
    val totaleOreValore = totaleOre * tariffaOraria

    val tabOre = PdfPTable(2)
    tabOre.widthPercentage = 100f
    tabOre.setWidths(floatArrayOf(1f, 1f))

    tabOre.addCell(PdfPCell(Paragraph("Ore totali lavorate:")).apply {
        border = Rectangle.NO_BORDER
    })
    tabOre.addCell(PdfPCell(Paragraph("%.2f".format(totaleOre))).apply {
        border = Rectangle.NO_BORDER
        horizontalAlignment = Element.ALIGN_RIGHT
    })

    tabOre.addCell(PdfPCell(Paragraph("Valore totale ore:")).apply {
        border = Rectangle.NO_BORDER
    })
    tabOre.addCell(PdfPCell(Paragraph("%.2f €".format(totaleOreValore))).apply {
        border = Rectangle.NO_BORDER
        horizontalAlignment = Element.ALIGN_RIGHT
    })

    documento.add(tabOre)

    documento.add(Paragraph("\n\n"))

    // ----------------------------------------------------
// SEZIONE MATERIALI
// ----------------------------------------------------
    documento.add(Paragraph("Materiale utilizzato:", Font(Font.HELVETICA, 14f, Font.BOLD)))
    documento.add(Paragraph("\n"))

    val tabMat = PdfPTable(3)
    tabMat.widthPercentage = 100f
    tabMat.setWidths(floatArrayOf(2f, 0.7f, 1f))

// Intestazioni tabella
    tabMat.addCell(PdfPCell(Paragraph("Descrizione", Font(Font.HELVETICA, 12f, Font.BOLD))))
    tabMat.addCell(PdfPCell(Paragraph("Quantità", Font(Font.HELVETICA, 12f, Font.BOLD))).apply {
        horizontalAlignment = Element.ALIGN_RIGHT
    })
    tabMat.addCell(PdfPCell(Paragraph("Prezzo", Font(Font.HELVETICA, 12f, Font.BOLD))).apply {
        horizontalAlignment = Element.ALIGN_RIGHT
    })

// Totale materiali
    var totaleMateriali = 0.0

    materialiRiepilogo.forEach { (materiale, quantita) ->
        val totale = materiale.prezzo * quantita
        totaleMateriali += totale

        tabMat.addCell("${materiale.marca} ${materiale.modello}")
        tabMat.addCell(
            PdfPCell(Paragraph("%.2f".format(quantita))).apply {
                horizontalAlignment = Element.ALIGN_RIGHT
            }
        )

        tabMat.addCell(
            PdfPCell(Paragraph("%.2f €".format(totale))).apply {
                horizontalAlignment = Element.ALIGN_RIGHT
            }
        )
    }

// --- RIGA TOTALE MATERIALI (DENTRO LA TABELLA) ---
    val cellTotLabel = PdfPCell(Paragraph("Totale materiali:", Font(Font.HELVETICA, 13f, Font.BOLD)))
    cellTotLabel.colspan = 2
    cellTotLabel.border = Rectangle.TOP
    tabMat.addCell(cellTotLabel)

    val cellTotValue = PdfPCell(Paragraph("%.2f €".format(totaleMateriali), Font(Font.HELVETICA, 13f, Font.BOLD)))
    cellTotValue.border = Rectangle.TOP
    cellTotValue.horizontalAlignment = Element.ALIGN_RIGHT
    tabMat.addCell(cellTotValue)

    documento.add(tabMat)
    documento.add(Paragraph("\n"))

    // ----------------------------------------------------
    // SALDO FINALE
    // ----------------------------------------------------
    val saldoFinale = totaleOreValore + totaleMateriali
    val fontFinale = Font(Font.HELVETICA, 16f, Font.BOLD)

    val tabFinale = PdfPTable(2)
    tabFinale.widthPercentage = 100f
    tabFinale.setWidths(floatArrayOf(1f, 1f))

    tabFinale.addCell(PdfPCell(Paragraph("Saldo finale:", fontFinale)).apply {
        border = Rectangle.NO_BORDER
    })

    tabFinale.addCell(PdfPCell(Paragraph("%.2f €".format(saldoFinale), fontFinale)).apply {
        border = Rectangle.NO_BORDER
        horizontalAlignment = Element.ALIGN_RIGHT
    })

    documento.add(tabFinale)

    // ----------------------------------------------------
    documento.close()
    println("PDF generato in: $path")
}