package com.traeper.demo.batch.configuration

import org.springframework.batch.core.configuration.BatchConfigurationException
import org.springframework.batch.core.configuration.annotation.BatchConfigurer
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.SimpleJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import javax.annotation.PostConstruct

@Configuration
@EnableBatchProcessing
class InMemoryBatchConfigurer : BatchConfigurer {
    private val transactionManager: PlatformTransactionManager = ResourcelessTransactionManager()
    private lateinit var jobRepository: JobRepository
    private lateinit var jobLauncher: JobLauncher
    private lateinit var jobExplorer: JobExplorer

    @Bean
    fun jobBuilderFactory(): JobBuilderFactory {
        return JobBuilderFactory(jobRepository)
    }

    @Bean
    fun stepBuilderFactory(): StepBuilderFactory {
        return StepBuilderFactory(jobRepository, transactionManager)
    }

    override fun getTransactionManager(): PlatformTransactionManager {
        return transactionManager
    }

    override fun getJobRepository(): JobRepository {
        return jobRepository
    }

    override fun getJobLauncher(): JobLauncher {
        return jobLauncher
    }

    override fun getJobExplorer(): JobExplorer {
        return jobExplorer
    }

    @PostConstruct
    fun initialize() {
        try {
            val jobRepositoryFactoryBean = MapJobRepositoryFactoryBean(this.transactionManager)
            jobRepositoryFactoryBean.afterPropertiesSet()
            this.jobRepository = jobRepositoryFactoryBean.getObject()

            val jobExplorerFactoryBean = MapJobExplorerFactoryBean(jobRepositoryFactoryBean)
            jobExplorerFactoryBean.afterPropertiesSet()
            this.jobExplorer = jobExplorerFactoryBean.getObject()

            val jobLauncher = SimpleJobLauncher()
            jobLauncher.setJobRepository(jobRepository)
            jobLauncher.afterPropertiesSet()
            this.jobLauncher = jobLauncher
        } catch (e: Exception) {
            throw BatchConfigurationException(e)
        }
    }
}
