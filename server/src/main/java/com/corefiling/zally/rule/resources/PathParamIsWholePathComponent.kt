package com.corefiling.zally.rule.resources

import com.corefiling.zally.rule.CoreFilingSwaggerRule
import com.corefiling.zally.rule.collections.pathParamRegex
import de.zalando.zally.dto.ViolationType
import de.zalando.zally.rule.Violation
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class PathParamIsWholePathComponent : CoreFilingSwaggerRule() {
    override val title = "Path Parameters Are Entire Path Components"
    override val violationType = ViolationType.MUST
    override val description = "Path parameters occupy an entire path component between slashes, never a partial component"

    override fun validate(swagger: Swagger): Violation? {

        val failures = mutableListOf<String>()

        swagger.paths?.forEach { pattern, _ ->

            val components = pattern.split('/')
            val failure = components
                    .map { pathParamRegex.replaceFirst(it, "XXXXX") }
                    .any { it !="XXXXX" }

            if (failure) {
                failures.add(pattern)
            }
        }

        return if (failures.isEmpty()) null else
            Violation(this, title, description, violationType, url, failures)
    }
}