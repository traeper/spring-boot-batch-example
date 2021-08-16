package com.traeper.demo.batch.configuration

import com.traeper.demo.batch.BatchType
import com.traeper.demo.batch.service.SampleBatchService
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import javax.annotation.PostConstruct

@Configuration
@Import(InMemoryBatchConfigurer::class)
class BatchConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val applicationContext: ConfigurableApplicationContext,
    private val sampleBatchService: SampleBatchService,
) {
    @PostConstruct
    fun init() {
        registerBatchBean(BatchType.SAMPLE) { sampleBatchService.execute() }
    }

    private fun registerBatchBean(batchType: BatchType, function: () -> Unit) {
        val step = stepBuilderFactory.get(batchType.name)
            .tasklet { _, _ ->
                function.invoke()
                RepeatStatus.FINISHED
            }
            .build()

        val job = jobBuilderFactory.get(batchType.name)
            .start(step)
            .build()

        val beanFactory = this.applicationContext.beanFactory
        beanFactory.registerSingleton("${batchType}_STEP", step)
        beanFactory.registerSingleton("${batchType}_JOB", job)
    }
}
