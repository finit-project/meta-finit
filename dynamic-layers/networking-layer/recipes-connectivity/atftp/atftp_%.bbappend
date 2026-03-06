inherit finit

SRC_URI:append = " file://atftpd.conf"

FINIT_PACKAGES = "${PN}d"
FINIT_SERVICES:${PN}d = "atftpd"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'finit', 'true', 'false', d)}; then
        install -d ${D}${sysconfdir}/default
        install -m 0644 ${WORKDIR}/atftpd.conf ${D}${sysconfdir}/default/atftpd
    fi
}

