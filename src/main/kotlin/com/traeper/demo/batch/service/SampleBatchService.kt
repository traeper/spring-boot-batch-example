package com.traeper.demo.batch.service

import com.traeper.demo.batch.BatchType
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.*

private val log = KotlinLogging.logger { }

@Service
class SampleBatchService {
    fun execute() {
        val batchId = UUID.randomUUID().toString()
        log.info { "BATCH-START - $batchId, ${BatchType.SAMPLE.name}" }

        val sleepSeconds = Random().nextInt(7)
        log.info { "sleep... ${sleepSeconds}s" }
        Thread.sleep(sleepSeconds * 1000L)

        log.info { "BATCH-END - $batchId, ${BatchType.SAMPLE.name}" }
    }
}
