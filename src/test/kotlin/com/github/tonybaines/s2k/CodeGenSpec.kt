package com.github.tonybaines.s2k

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object CodeGenSpecSpec : Spek({
    given("Previously compiled code") {
        on("calling generated code") {
            val foo = org.foo.bar.Foo(
                    foo = "Foo",
                    bar = org.foo.bar.Bar(
                            bar = 1,
                            cats = arrayListOf<String>("Kitty", "Katty")
                    ),
                    baz = true,
                    bang = 42.0
            )
            it("will be available") {
                foo.bar.cats[1] == "Katty"
            }
        }
    }
})