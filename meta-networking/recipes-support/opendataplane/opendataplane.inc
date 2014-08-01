SUMMARY = "Cross platform OpenDataPlane application APIs"

DESCRIPTION = "The OpenDataPlane project has been established to \
produce an open-source, cross-platform set of application \
programming interfaces (APIs) for the networking data plane."

HOMEPAGE = "http://www.opendataplane.org"
SECTION = "networking"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4ccfa994aa96974cfcd39a59faee20a2"
                 
SRC_URI = "git://git.linaro.org/lng/odp.git"

COMPATIBLE_HOST = "^((?!mips).*)$"

PACKAGECONFIG ??= "platform_linux_generic"
PACKAGECONFIG[netmap] = "--enable-netmap,,,"
PACKAGECONFIG[platform_linux_generic] = "--with-platform=linux-generic,,,"

S = "${WORKDIR}/git"

