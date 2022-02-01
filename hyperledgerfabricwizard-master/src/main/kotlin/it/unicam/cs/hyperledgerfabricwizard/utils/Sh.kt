package it.unicam.cs.hyperledgerfabricwizard.utils

abstract class Sh {
    override fun toString(): String {
        //export PATH=/home/luca/Documenti/fabric-samples/bin:$PATH
        return "#!/bin/bash\ncd \$(dirname \$0)\n"
    }
}