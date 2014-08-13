require ${BPN}.inc

SRC_URI += "\
    file://0001-OptionsCommon.cmake-don-t-mix-CXX_FLAGS-into-C_FLAGS.patch \
    file://0002-WebKitHelpers.cmake-Add-Wno-error-deprecated-declara.patch \
    file://0003-FEBlendNEON.h-fix-missing-semicolon.patch \
"
SRC_URI[md5sum] = "90fa970ebf8646319d292c2bb5bff5db"
SRC_URI[sha256sum] = "d8d21e27f4a21cd77c41914548c184ddb98693ba23851aa66c8e51c0be4b90b7"
