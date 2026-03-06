inherit finit

FINIT_SERVICES:${PN} = "kea-dhcp4"

FILES:${PN} += "${libdir}/tmpfiles.d"

do_install:append () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'finit', 'true', 'false', d)}; then
        install -d ${D}${libdir}/tmpfiles.d
        echo "d /run/kea - - - -" > ${D}${libdir}/tmpfiles.d/kea.conf
    fi
}
