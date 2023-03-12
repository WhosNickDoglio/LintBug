package dev.whosnickdoglio.lint

import dagger.Provides


class MyModule {

    @Provides fun doSomething(): String = "Hello World"
}
