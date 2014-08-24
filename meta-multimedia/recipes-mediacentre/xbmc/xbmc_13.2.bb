SUMMARY = "XBMC Media Center"
DESCRIPTION = "XBMC is an award-winning free and open source (GPL) software \
media player and entertainment hub that can be installed on Linux, OSX, \
Windows, iOS, and Android, featuring a 10-foot user interface for use with \
televisions and remote controls."
HOMEPAGE = "http://xbmc.org/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.GPL;md5=6eb631b6da7fdb01508a80213ffc35ff"

DEPENDS = "yajl libxmu fribidi mpeg2dec curl python libmodplug libmicrohttpd \
           sqlite3 libcdio libpcre boost lzo taglib libtinyxml jasper libass \
           libmad jpeg libsamplerate0 libvorbis tiff libxslt libgpg-error \
           libsdl virtual/egl swig-native gperf-native unzip-native zip-native \
           libcec rtmpdump"

DEPENDS_append_arm = " cmake-native"
DEPENDS_append_x86 = " nasm-native"

CODENAME = "Gotham"
SRCREV = "0f3db0516711e05765d297d060563730131c2f92"
SRC_URI = "git://github.com/xbmc/xbmc.git;branch=${CODENAME} \
           file://0001-configure-don-t-run-python-distutils-to-find-STAGING.patch \
           file://0002-Revert-fixed-ios-Add-memory-barriers-to-atomic-Add-S.patch \
           file://0003-Revert-fixed-ios-Add-memory-barriers-to-cas-assembly.patch \
           file://0004-Support-for-novfpnoneon-platforms.patch"

inherit autotools-brokensep gettext python-dir

S = "${WORKDIR}/git"

# Don't activate texturepacker as it needs libsdl-image-native. Due to some issues
# with qemu we use the host's libsdl on which libsdl-image-native depends.
PACKAGECONFIG ??= "${@base_contains('DISTRO_FEATURES', 'opengl', 'opengl', 'opengles2', d)} \
                   sdl airplay ssh libusb libcec samba mysql avahi xrandr x11 joystick \
                   mid rtmp libmp3lame optical-drive debug"

PACKAGECONFIG[opengl] = "--enable-gl,--enable-gles,glew"
PACKAGECONFIG[opengles2] = "--enable-gles,--enable-gl,"
PACKAGECONFIG[sdl] = "--enable-sdl,--disable-sdl,libsdl-mixer libsdl-image"
PACKAGECONFIG[airplay] = "--enable-airplay,--disable-airplay,libplist"
PACKAGECONFIG[ssh] = "--enable-ssh,--disable-ssh,libssh"
PACKAGECONFIG[libcec] = "--enable-libcec,--disable-libcec,libcec"
PACKAGECONFIG[libusb] = "--enable-libusb,--disable-libusb,libusb"
PACKAGECONFIG[samba] = "--enable-samba,--disable-samba,samba"
PACKAGECONFIG[mysql] = "--enable-mysql,--disable-mysql,mysql5"
PACKAGECONFIG[avahi] = "--enable-avahi,--disable-avahi,avahi"
PACKAGECONFIG[xrandr] = "--enable-xrandr,--disable-xrandr,libxrandr"
PACKAGECONFIG[x11] = "--enable-x11,--disable-x11,"
PACKAGECONFIG[joystick] = "--enable-joystick,--disable-joystick,"
PACKAGECONFIG[texturepacker] = "--enable-texturepacker,--disable-texturepacker,libsdl-image-native"
PACKAGECONFIG[mid] = "--enable-mid,--disable-mid,"
PACKAGECONFIG[rtmp] = "--enable-rtmp,--disable-rtmp,rtmpdump"
PACKAGECONFIG[optical-drive] = "--enable-optical-drive,--disable-optical-drive,libcdio"
PACKAGECONFIG[libmp3lame] = "--enable-libmp3lame,--disable-libmp3lame,lame"
PACKAGECONFIG[debug] = "--enable-debug,--disable-debug,"

# No neon / no VFP on ARMv4 and ARMv5
EXTRA_OECONF_append_armv4 = " --with-platform=novfpnoneon "
EXTRA_OECONF_append_armv5 = " --with-platform=novfpnoneon "

EXTRA_OECONF_append = " \
    --enable-external-libraries \
    --with-arch=${TARGET_ARCH} \
    "

FULL_OPTIMIZATION_armv7a = "-fexpensive-optimizations -fomit-frame-pointer -O4 -ffast-math"
BUILD_OPTIMIZATION = "${FULL_OPTIMIZATION}"

EXTRA_OECONF_append_armv7a = "--with-cpu=cortex-a8"

CACHED_CONFIGUREVARS += " \
    ac_cv_path_PYTHON="${STAGING_BINDIR_NATIVE}/python-native/python" \
"

# for python modules
export HOST_SYS
export BUILD_SYS
export STAGING_LIBDIR
export STAGING_INCDIR
export PYTHON_DIR

do_configure() {
    sh bootstrap
    oe_runconf
}

# On ARM don't run GL/X11 tests in xbmc shell wrapper as xbmc uses GLES
do_configure_prepend_arm() {
    sed -i '/FEH.py/ s/$/ --no-test/' ${S}/tools/Linux/xbmc.sh.in
}

FILES_${PN} += " \
    ${datadir}/xsessions \
    ${datadir}/icons"
FILES_${PN}-dbg += " \
    ${libdir}/xbmc/.debug \
    ${libdir}/xbmc/*/.debug \
    ${libdir}/xbmc/*/*/.debug \
    ${libdir}/xbmc/*/*/*/.debug \
    "

# XBMC uses some kind of dlopen() method for libcec so we need to add it manually.
# XBMC wrapper calls xdpyinfo.
# OpenGL builds need glxinfo, that's in mesa-demos.
# service.py and other python tools need a couple of additional modules.
RRECOMMENDS_${PN}_append = " \
    libcec \
    libcurl \
    ${@base_contains('DISTRO_FEATURES', 'x11', 'xdpyinfo', '', d)} \
    ${@base_contains('DISTRO_FEATURES', 'opengl', 'mesa-demos', '', d)} \
    python python-stringold python-codecs python-re python-lang python-netclient\
    "

RRECOMMENDS_${PN}_append_libc-glibc = " \
    glibc-charmap-ibm850 glibc-charmap-utf-8 \
    glibc-gconv-ibm85 glibc-gconv-utf-32 \
    "

RDEPENDS_${PN} = " libpulsecommon"
