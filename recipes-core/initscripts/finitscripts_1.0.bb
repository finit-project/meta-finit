SUMMARY = "Init scripts for finit"
DESCRIPTION = "Sysvinit style scripts with matching finit configurations."
SECTION = "base"
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"

RPROVIDES:${PN} += "initd-functions"

SRC_URI = " \
    file://functions \
    file://devconsole.conf \
"

do_install () {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/functions ${D}${sysconfdir}/init.d

    install -d ${D}${sysconfdir}/finit.d
    install -D -m 0644 ${WORKDIR}/devconsole.conf ${D}${sysconfdir}/finit.d/devconsole.conf
}

CONFFILES:${PN} = "${sysconfdir}/init.d"
