package dev.whosnickdoglio.checks

import com.android.tools.lint.checks.infrastructure.TestFiles
import com.android.tools.lint.checks.infrastructure.TestLintTask
import org.junit.Test

class MissingModuleAnnotationTest {
    private val daggerStubs =
        TestFiles.kotlin(
            """
        package  dagger

        annotation class Provides
        annotation class Binds
        annotation class Module
    """
                .trimIndent()
        )

    @Test
    fun `module without annotation shows an error`() {
        TestLintTask.lint()
            .files(
                daggerStubs,
                TestFiles.kotlin(
                    """
                package com.test.android

                import dagger.Provides

                 class MyModule {

                        @Provides
                        fun doSomething(): String = "Hello"
                }
                """
                )
                    .indented()
            )
            .issues(MissingModuleAnnotation.ISSUE)
            .run()
            .expect(
                """
                    src/com/test/android/MyModule.kt: Error: Don't forget the @Module annotation! [MissingModuleAnnotation]
                    1 errors, 0 warnings
                """
                    .trimIndent()
            )
            .expectErrorCount(1)
    }
}
