inherit finit

FINIT_SERVICES:${PN} = "weston"
FINIT_SYSVINIT_INHIBIT = "0"

do_install:append() {
    # Finit does not support running weston with non-root users
    if ${@bb.utils.contains('DISTRO_FEATURES', 'finit', 'true', 'false', d)}; then
        sed -i -e 's#WESTON_USER=weston#WESTON_USER=root#' ${D}/${sysconfdir}/init.d/weston
        sed -i -e 's#OPTARGS &#OPTARGS#' ${D}/${sysconfdir}/init.d/weston
    fi
}
