package com.github.tonybaines.s2k

import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class DefinitionToKotlinTask extends DefaultTask {
    @InputFile File swaggerFile
    String basePath
    String pkg

    @OutputDirectory
    File getOutputDir() {
        return project.file("$basePath/${pkg.replace('.', '/')}")
    }

    @TaskAction
    def generateKotlinDataClasses() {
        def json = new JsonSlurper().parse(swaggerFile)

        json['definitions'].each { name, definition ->
            def pkgDir = "$basePath/${pkg.replace('.', '/')}"
            def sourceFilePath = "$pkgDir/${name}.kt"
            def source = new File(sourceFilePath)
//            new File(pkgDir).mkdirs()
            source.createNewFile()

            def sourceCode = new StringBuffer("package $pkg\n\n")
            sourceCode.append("data class $name(\n")
            def props = definition['properties']
            sourceCode.append(props
                    .collect { pName, pDef -> [pName, toKotlin(pDef)] }
                    .collect { pName, kotlinType -> "  val $pName: $kotlinType" }
                    .join(',\n')
            )
            sourceCode.append('\n)')
            source.text = sourceCode
            source
        }
    }

    def toKotlin(definition) {
        switch (definition['type']) {
            case 'string': return 'String'
            case 'boolean': return 'Boolean'
            case 'integer': return 'Int'
            case 'number': return 'Double'
            case 'object': return definition['$ref'].split('/').last()
            case 'array': return "List<${toKotlin(definition['items'])}>"
        }
    }
}