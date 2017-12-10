package com.github.tonybaines.s2k

import spock.lang.Specification

class DefintionToKotlinTest extends Specification {
    def "can generate a simple class from a simple JSON model"() {
        when:
        new DefintionToKotlin().generateFrom('src/test/resources/simple-openapi-2.0.json')

        then:
        new File('build/generated/sources/foo/Foo.kt').exists()
    }
}
