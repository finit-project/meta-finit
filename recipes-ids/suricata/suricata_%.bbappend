inherit finit

FILES:${PN} += "${libdir}/tmpfiles.d/suricata.conf"

do_install:append () {
    install -d ${D}${libdir}/tmpfiles.d
    install -m 0644 ${WORKDIR}/tmpfiles.suricata ${D}${libdir}/tmpfiles.d/suricata.conf
}
