<template>
    <div class="max-w-3xl">
        <div class="page-head">
            <h1 class="page-title">Términos y Condiciones de Uso</h1>
            <p class="page-sub">Última actualización conforme a la operación de la plataforma.</p>
        </div>

        <article class="card card-pad prose prose-slate max-w-none dark:prose-invert">
            <h3>1. Definiciones y Roles</h3>
            <p>Para la transparencia de esta plataforma, se establecen los siguientes roles:</p>
            <ul>
                <li><strong>Desarrollador (GDG Guadalajara):</strong> Autor y mantenedor del código fuente original. Provee la lógica de software de manera gratuita y abierta.</li>
                <li><strong>Anfitrión de Infraestructura (Host):</strong> Entidad independiente que despliega, opera y gestiona esta instancia específica. Responsable de las credenciales <strong>OAuth2</strong> y la persistencia de datos.</li>
                <li><strong>Organización Emisora (Issuer):</strong> Entidad registrada que utiliza el sistema para otorgar acreditaciones digitales.</li>
                <li><strong>Recipiente:</strong> Usuario final que recibe y gestiona sus insignias.</li>
            </ul>

            <h3>2. Naturaleza del Software y Deslinde de Garantías</h3>
            <p>El software se proporciona <strong>"tal cual" (as-is)</strong>.</p>
            <ul>
                <li><strong>GDG Guadalajara</strong>, en su calidad de Desarrollador, no otorga garantías sobre la idoneidad, seguridad o ausencia de errores en el código.</li>
                <li>El uso de la plataforma no crea una relación comercial ni de prestación de servicios profesionales entre las partes.</li>
            </ul>

            <h3>3. Responsabilidad del Anfitrión (Host)</h3>
            <p>El <strong>Host</strong> es el único responsable de la operación técnica de esta instancia, incluyendo:</p>
            <ul>
                <li>La correcta configuración y resguardo de las credenciales de autenticación (Client IDs y Secrets).</li>
                <li>La seguridad lógica de los servidores y bases de datos.</li>
                <li>La disponibilidad del servicio y el soporte técnico a las Organizaciones Emisoras.</li>
            </ul>

            <h3>4. Responsabilidad de la Información</h3>
            <ul>
                <li><strong>Veracidad:</strong> La legitimidad de las insignias y el cumplimiento de sus criterios son responsabilidad exclusiva de la <strong>Organización Emisora</strong>.</li>
                <li><strong>Privacidad Técnica:</strong> Se utiliza el estándar <strong>Open Badges</strong>; el correo del recipiente se procesa mediante un <strong>hash SHA-256</strong> para proteger su identidad en los metadatos públicos.</li>
            </ul>

            <h3>5. Propiedad y Portabilidad</h3>
            <p>El Recipiente posee una licencia perpetua para compartir y descargar su insignia "horneada" (con metadatos inyectados). El Emisor se reserva el derecho de <strong>revocar</strong> una insignia únicamente en casos de error administrativo, fraude probado o violación a los códigos de conducta de la comunidad.</p>

            <h3>6. Modificaciones y Jurisdicción</h3>
            <p>El Host se reserva el derecho de actualizar estos términos para adaptarlos a cambios técnicos o legales en su jurisdicción. El uso continuo de la plataforma tras el login implica la aceptación de la versión vigente.</p>
        </article>
    </div>
</template>
