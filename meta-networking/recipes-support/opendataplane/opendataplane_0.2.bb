require ${BPN}.inc

#odp-0.2 tag
SRCREV  = "6007d8833a6880abedf98f792586c0cfb9f10de0"
PV = "0.2"

#0.2 still using pure Makefile

#override the Makefile DESTDIR
export DESTDIR="${D}${exec_prefix}"

do_install () {
    oe_runmake install
    
    #move DESTDIR/share/odp/odp_x usr/bin
    install -d ${D}${bindir}
    for i in ${DESTDIR}/share/odp/*; do
        filename=`basename $i`
        mv ${DESTDIR}/share/odp/${filename} ${D}${bindir}/${filename}
    done
    rm -rf ${DESTDIR}/share
}

#non standard doc install location
FILES_${PN}-doc += "${exec_prefix}/doc/*"
