package org.example.printdata

import java.io.File
import java.io.FileOutputStream

fun generaPdf(nome: String, ore: Double) {
    val document = com.lowagie.text.Document()
    val file = File("C:\\rapportino.pdf")
    com.lowagie.text.pdf.PdfWriter.getInstance(document, FileOutputStream(file))

    document.open()
    document.add(com.lowagie.text.Paragraph("Rapportino"))
    document.add(com.lowagie.text.Paragraph(""))
    document.add(com.lowagie.text.Paragraph("Nome: $nome"))
    document.add(com.lowagie.text.Paragraph("Ore lavorate: $ore"))
    document.close()
}