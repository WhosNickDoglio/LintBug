package dev.whosnickdoglio.checks

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class MyTestIssueRegistry: IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(
            MissingModuleAnnotation.ISSUE,
        )

    override val api: Int = CURRENT_API

    override val vendor: Vendor = Vendor(
        vendorName = "Test",
        feedbackUrl = "test.com",
        contact = "hello@hello.com"
    )
}
