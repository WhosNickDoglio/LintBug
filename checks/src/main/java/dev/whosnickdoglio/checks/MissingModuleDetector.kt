package dev.whosnickdoglio.checks

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement

class MissingModuleAnnotation : Detector(), SourceCodeScanner {

    private val annotations = listOf("dagger.Binds", "dagger.Provides")

    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(UClass::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler =
        object : UElementHandler() {
            override fun visitClass(node: UClass) {
                if (
                    !node.uAnnotations.any { annotations ->
                        annotations.qualifiedName == "dagger.Module"
                    }
                ) {
                    val moduleMethods =
                        node.methods
                            .flatMap { method -> method.uAnnotations }
                            .filter { annotation -> annotation.qualifiedName in annotations }

                    if (moduleMethods.isNotEmpty()) {
                        context.report(
                            issue = ISSUE,
                            location = context.getLocation(node.javaPsi),
                            message = "Don't forget the @Module annotation!",
                            quickfixData =
                            fix()
                                .annotate("dagger.Module")
                                .range(context.getNameLocation(node))
                                .build()
                        )
                    }
                }
            }
        }

    companion object {
        private val implementation =
            Implementation(MissingModuleAnnotation::class.java, Scope.JAVA_FILE_SCOPE)
        val ISSUE =
            Issue.create(
                id = "MissingModuleAnnotation",
                briefDescription = "Hello friend",
                explanation = "Hello friend",
                category = Category.CORRECTNESS,
                priority = 5,
                severity = Severity.ERROR,
                implementation = implementation
            )
    }
}
