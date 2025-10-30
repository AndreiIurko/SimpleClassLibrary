import org.intellij.lang.annotations.Language
import com.intellij.psi.*
import org.jetbrains.kotlin.psi.*

/**
 * Direct console output using println should be replaced with a proper logging framework.
 * Console output is difficult to control in production environments, while logging frameworks
 * provide configurable log levels, formatting, and output destinations.
 */
@Language("HTML")
val htmlDescription = """
    <html>
    <body>
        Direct console output using println should be replaced with a proper logging framework.
        Console output is difficult to control in production environments, while logging frameworks
        provide configurable log levels, formatting, and output destinations.
    </body>
    </html>
""".trimIndent()

val noPrintlnInspection = localInspection { psiFile, inspection ->
    fun isPrintlnCall(call: KtCallExpression): Boolean {
        val calleeExpression = call.calleeExpression as? KtNameReferenceExpression ?: return false
        return calleeExpression.getReferencedName() == "println"
    }
    
    val printlnCalls = psiFile.descendantsOfType<KtCallExpression>()
        .filter { call -> isPrintlnCall(call) }
    
    printlnCalls.forEach { call ->
        inspection.registerProblem(call, "Replace println with proper logging framework")
    }
}

listOf(
    InspectionKts(
        id = "NoPrintlnInspection",
        localTool = noPrintlnInspection,
        name = "No println usage",
        htmlDescription = htmlDescription,
        level = HighlightDisplayLevel.WARNING,
    )
)