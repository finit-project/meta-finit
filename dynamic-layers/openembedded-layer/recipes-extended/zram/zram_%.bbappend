inherit finit

FINIT_SYSVINIT_INHIBIT = "0"

do_install () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'finit', 'true', 'false', d)}; then
        install -d ${D}${sysconfdir}/init.d
        install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/zram
    fi
}
