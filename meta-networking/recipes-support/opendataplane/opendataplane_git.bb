require ${BPN}.inc

SRC_URI += "file://0001-linux-dpdk-remove-unnecessary-include-path.patch"

SRCREV  = "68a45b11e52dbf835a7087c15d503c6b0e5d91ed"
#8f4f080625e2db810da3f13770ba418c72b08ede will support --with-sdk-install-path
PV = "0.2+git${SRCPV}"

PACKAGECONFIG[platform_linux_generic] = "--with-platform=linux-generic,,openssl,libcrypto"
#need meta-intel layer's common/recipes-extended/dpdk/dpdk recipe
PACKAGECONFIG[platform_linux_dpdk] = '--with-platform=linux-dpdk --with-sdk-install-path="${STAGING_EXECPREFIXDIR}",,dpdk,'
PACKAGECONFIG[platform_linux_keystone2] = "--with-platform=linux-keystone2,,,"

inherit autotools
