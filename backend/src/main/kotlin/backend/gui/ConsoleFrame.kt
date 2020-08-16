package backend.gui

import java.awt.BorderLayout
import java.awt.Font
import java.io.IOException
import java.io.OutputStream
import java.io.PrintStream
import javax.swing.*
import javax.swing.text.DefaultCaret

/** A swing frame that shows console output.  */
object ConsoleFrame {
    fun show() {
        SwingUtilities.invokeLater { _show() }
    }

    private fun _show() {
        val textArea = JTextArea(20, 100)
        textArea.font = Font(Font.MONOSPACED, Font.PLAIN, 12)
        textArea.isEditable = false

        // Enable auto scrolling.
        val caret = textArea.caret as DefaultCaret
        caret.updatePolicy = DefaultCaret.ALWAYS_UPDATE
        val taOutputStream = TextAreaOutputStream(textArea)
        System.setOut(PrintStream(taOutputStream, true))
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(JScrollPane(textArea))
        val frame = JFrame("Console")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.contentPane.add(panel)
        frame.pack()
        // Locate the frame in the center of the screen.
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }

    internal class TextAreaOutputStream(private val textArea: JTextArea) : OutputStream() {
        @Throws(IOException::class)
        override fun write(b: Int) {
            textArea.append(b.toChar().toString())
        }
    }
}