package it.unicam.cs.hyperledgerfabricwizard.fabric.scripts

import it.unicam.cs.hyperledgerfabricwizard.fabric.Globals
import it.unicam.cs.hyperledgerfabricwizard.utils.Sh
import it.unicam.cs.hyperledgerfabricwizard.utils.appendNewline

class PrerequisitesSh : Sh() {
    val output = buildString {
        appendNewline(
                """
VERSION=${Globals.VERSION}
CA_VERSION=${Globals.CA_VERSION}
ARCH=${'$'}(echo "${'$'}(uname -s|tr '[:upper:]' '[:lower:]'|sed 's/mingw64_nt.*/windows/')-${'$'}(uname -m | sed 's/x86_64/amd64/g')")
MARCH=${'$'}(uname -m)


download() {
local BINARY_FILE=${'$'}1
local URL=${'$'}2
echo "===> Downloading from: " "${'$'}{URL}"
curl -L --retry 5 --retry-delay 3 "${'$'}{URL}" | tar xz || rc=${'$'}?
if [ -n "${'$'}rc" ]; then
    echo "==> Error"
    return 22
else
    echo "==> Done."
fi
}

pullBinaries() {
download "${'$'}{BINARY_FILE}" "https://github.com/hyperledger/fabric/releases/download/v${'$'}{VERSION}/${'$'}{BINARY_FILE}"
if [ ${'$'}? -eq 22 ]; then
    echo
    echo "------> Error"
    echo
    exit
fi

download "${'$'}{CA_BINARY_FILE}" "https://github.com/hyperledger/fabric-ca/releases/download/v${'$'}{CA_VERSION}/${'$'}{CA_BINARY_FILE}"
if [ ${'$'}? -eq 22 ]; then
    echo
    echo "------> Error"
    echo
    exit
fi
}


if [[ ${'$'}VERSION =~ ^1\.[0-1]\.* ]]; then
export FABRIC_TAG=${'$'}{MARCH}-${'$'}{VERSION}
export CA_TAG=${'$'}{MARCH}-${'$'}{CA_VERSION}
export THIRDPARTY_TAG=${'$'}{MARCH}-${'$'}{THIRDPARTY_IMAGE_VERSION}
else
: "${'$'}{CA_TAG: = "${'$'}CA_VERSION"}"
: "${'$'}{FABRIC_TAG: = "${'$'}VERSION"}"
: "${'$'}{THIRDPARTY_TAG: = "${'$'}THIRDPARTY_IMAGE_VERSION"}"
fi

BINARY_FILE=hyperledger-fabric-${'$'}{ARCH}-${'$'}{VERSION}.tar.gz
CA_BINARY_FILE=hyperledger-fabric-ca-${'$'}{ARCH}-${'$'}{CA_VERSION}.tar.gz

echo "Downloading HyperledgerFabric binaries"
pullBinaries
rm -r config  
echo Remember to add the binaries path to your PATH environment variable otherwise scripts won\'t be usable. 
                """.trimIndent()
        )
    }

    override fun toString(): String {
        return super.toString() + output
    }
}