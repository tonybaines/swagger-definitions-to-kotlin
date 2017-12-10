package com.github.tonybaines.s2k

import groovy.json.JsonSlurper

class DefintionToKotlin {

    def generateFrom(path, String basePath = "build/generated/sources", String pkg="foo") {
        def json = new JsonSlurper().parse(new File(path))

        json['definitions'].each { name, definition ->
            def pkgDir = "$basePath/${pkg.replace('.', '/')}"
            def sourceFilePath = "$pkgDir/${name}.kt"
            println(sourceFilePath)
            def source = new File(sourceFilePath)
            new File(pkgDir).mkdirs()
            source.createNewFile()

            def sourceCode = new StringBuffer("package $pkg\n\n")
            sourceCode.append("data class $name(\n")
            def props = definition['properties']
            sourceCode.append(props
                    .collect { pName, pDef -> [pName, pDef['type']] }
                    .collect { pName, jsonType -> [pName, toKotlin(jsonType)]}
                    .collect { pName, kotlinType -> "  val $pName: $kotlinType" }
                    .join(',\n')
            )
            sourceCode.append('\n)')
            source.text = sourceCode
            source
        }
    }

    private def toKotlin(type) {
        switch (type) {
            case 'string': return 'String'
            case 'boolean': return 'Boolean'
            case 'integer': return 'Int'
        }
    }
}
