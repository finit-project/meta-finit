inherit finit

FILES:${PN} += "${libdir}/tmpfiles.d"

do_install:append () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'finit', 'true', 'false', d)}; then
        install -d ${D}${libdir}/tmpfiles.d
        echo "d ${localstatedir}/log/sa - - - -" > ${D}${libdir}/tmpfiles.d/sysstat.conf
    fi
}
