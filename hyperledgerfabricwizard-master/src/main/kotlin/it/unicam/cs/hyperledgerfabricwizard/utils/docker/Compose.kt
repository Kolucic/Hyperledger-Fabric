package it.unicam.cs.hyperledgerfabricwizard.utils.docker

import com.fasterxml.jackson.annotation.JsonInclude
import it.unicam.cs.hyperledgerfabricwizard.utils.Builder
import it.unicam.cs.hyperledgerfabricwizard.utils.toYaml

/**
 * La classe identifica un file docker-compose per creare container docker.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class Compose private constructor(
        val version: String?,
        val services: Map<String, Service>?,
        val networks: Map<String, Any?>?,
        val volumes: Map<String, Any?>?
) {
    open class ComposeBuilder : Builder<Compose> {
        private var version: String? = null
        private var services: MutableMap<String, Service>? = null
        private var volumes: MutableMap<String, Any?>? = null
        private var networks: MutableMap<String, Any?>? = null

        fun version(version: Int) = apply {
            this.version = "$version"
        }

        fun addService(serviceName: String, service: Service) = apply {
            this.services = (this.services ?: mutableMapOf()).apply {
                this[serviceName] = service
            }
        }

        fun addNetwork(network: String) = apply {
            this.networks = (this.networks ?: mutableMapOf()).apply {
                this[network] = null
            }
        }

        fun addVolume(volumeName: String) = apply {
            this.volumes = (this.volumes ?: mutableMapOf()).apply {
                this[volumeName] = null
            }
        }


        override fun build() = Compose(version, services, networks, volumes)
    }

    override fun toString(): String {
        return this.toYaml()
    }

    companion object {
        fun builder() = ComposeBuilder()
    }
}