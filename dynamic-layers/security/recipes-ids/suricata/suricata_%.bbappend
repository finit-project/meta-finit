inherit finit

FILES:${PN} += "${nonarch_libdir}/tmpfiles.d"

do_install:append () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'finit', 'true', 'false', d)}; then
        install -d ${D}${nonarch_libdir}/tmpfiles.d
        install -m 0644 ${WORKDIR}/tmpfiles.suricata ${D}${nonarch_libdir}/tmpfiles.d/suricata.conf
    fi
}
