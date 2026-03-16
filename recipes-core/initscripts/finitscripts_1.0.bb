SUMMARY = "Init scripts for finit"
DESCRIPTION = "Sysvinit style scripts with matching finit configurations."
SECTION = "base"
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"

RPROVIDES:${PN} += "initd-functions"

inherit finit

FINIT_SERVICES:${PN} = "devconsole ${@'developer' if oe.types.boolean(d.getVar('FINIT_DEVELOPER')) else ''}"

# When being enabled, a devconsole would be started for users to login
FINIT_DEVELOPER ?= "1"

SRC_URI = " \
    file://functions \
    file://developer.sh \
"

do_install () {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/functions ${D}${sysconfdir}/init.d

    if "${@'true' if oe.types.boolean(d.getVar('FINIT_DEVELOPER')) else 'false'}"; then
        install -m 0755 ${WORKDIR}/developer.sh ${D}${sysconfdir}/init.d
    fi
}

CONFFILES:${PN} = "${sysconfdir}/init.d"
