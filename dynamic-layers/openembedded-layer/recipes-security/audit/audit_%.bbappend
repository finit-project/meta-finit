FILES:${PN} += "${nonarch_libdir}/tmpfiles.d"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'finit', 'true', 'false', d)}; then
        install -d ${D}${nonarch_libdir}/tmpfiles.d
        install -D -m 0644 ${WORKDIR}/audit-volatile.conf ${D}${nonarch_libdir}/tmpfiles.d/audit.conf
    fi
}
