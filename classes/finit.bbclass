python () {
    for pkg in (d.getVar('FINIT_PACKAGES') or "").split():
        # Sanity check, finit package must be included in PACKAGES
        mlprefix = d.getVar('MLPREFIX')
        if mlprefix and not pkg.startswith('%s' % mlprefix):
            finit_pkg = mlprefix + pkg
        else:
            finit_pkg = pkg
        packages = (d.getVar('PACKAGES') or "").split()
        if finit_pkg not in packages:
            raise bb.parse.SkipRecipe("FINIT_PACKAGES (%s) must be a subset of PACKAGES (%s)." % (finit_pkg, packages))
        # Sanity check, FINIT_SERVICES must be defined
        if not d.getVar('FINIT_SERVICES:%s' % pkg):
            raise bb.parse.SkipRecipe("FINIT_SERVICES:%s must be defined for %s package." % (pkg, pkg))

        for finit_service in (d.getVar('FINIT_SERVICES:%s' % pkg) or "").split():
            files_var = "FILES:" + finit_pkg
            srcuri_var = "SRC_URI"

            d.appendVar(srcuri_var, " file://%s.finit" % finit_service)
            d.appendVar(files_var, " %s/finit.d/%s.conf" % (d.getVar('sysconfdir'), finit_service))
            d.appendVar(files_var, " %s/finit.d/available/%s.conf" % (d.getVar('sysconfdir'), finit_service))

    # Inhibit update-rc.d
    d.setVar('INHIBIT_UPDATERCD_BBCLASS', 1)
}

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

FINIT_PACKAGES ?= "${PN}"
FINIT_SERVICES:${PN} ?= "${BPN}"
FINIT_SYSVINIT_INHIBIT ?= "1"
# To set auto enable for a specific service in case there are multiple finit services:
# FINIT_AUTO_ENABLE[service1] = "enabled"
# FINIT_AUTO_ENABLE[service2] = "available"
#
# The service1, service2 are items listed in FINIT_SERVICES_${PN}.
FINIT_AUTO_ENABLE ?= "enabled"

python finit_collect_services () {
    services = []
    for finit_pkg in (d.getVar('FINIT_PACKAGES') or "").split():
        for finit_service in (d.getVar('FINIT_SERVICES:%s' % finit_pkg) or "").split():
            service_enabled = d.getVarFlag('FINIT_AUTO_ENABLE', finit_service) or d.getVar('FINIT_AUTO_ENABLE')
            services.append('%s:%s' % (finit_service, service_enabled))
    d.setVar('FINIT_INSTALL_SERVICES', " ".join(services))
}

finit_install_services () {
    install -d ${D}${sysconfdir}/finit.d
    for service in ${FINIT_INSTALL_SERVICES}; do
        service_file=$(echo $service | cut -d ':' -f 1)
        service_enabled=$(echo $service | cut -d ':' -f 2)
        if [ "$service_enabled" = "enabled" ]; then
            install -m 0644 ${WORKDIR}/"$service_file".finit ${D}${sysconfdir}/finit.d/"$service_file".conf
        elif [ "$service_enabled" = "available" ]; then
            install -d ${D}${sysconfdir}/finit.d/available
            install -m 0644 ${WORKDIR}/"$service_file".finit ${D}${sysconfdir}/finit.d/available/"$service_file".conf
        else
            bbfatal "Inavlid FINIT_AUTO_ENABLE value for $service_file: $service_enabled, only 'enabled' or 'available' is valid."
        fi
    done

    if [ ${@ oe.types.boolean('${FINIT_SYSVINIT_INHIBIT}')} = True ]; then
        rm -rf ${D}${sysconfdir}/init.d
    fi
}
do_install[prefuncs] += "finit_collect_services"
do_install[postfuncs] += "finit_install_services"
