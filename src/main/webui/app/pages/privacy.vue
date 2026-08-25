<template>
    <div class="max-w-3xl">
        <div class="page-head">
            <h1 class="page-title">Aviso de Privacidad</h1>
            <p class="page-sub">Cómo tratamos, resguardamos y protegemos tus datos personales.</p>
        </div>

        <article class="card card-pad prose prose-slate max-w-none dark:prose-invert">
            <h3>1. Responsable del Tratamiento</h3>
            <p>El <strong>Anfitrión de Infraestructura (Host)</strong> de esta instancia es el responsable del tratamiento, resguardo y administración de sus datos personales. El <strong>Desarrollador (GDG Guadalajara)</strong> provee el código fuente pero no tiene acceso, propiedad ni control sobre la base de datos o las credenciales de esta implementación.</p>

            <h3>2. Datos Personales Recabados</h3>
            <p>Recabamos la información necesaria para gestionar sus acreditaciones digitales:</p>
            <ul>
                <li><strong>Vía Google OAuth2:</strong> Nombre completo, correo electrónico y fotografía de perfil.</li>
                <li><strong>Vía Organización Emisora:</strong> Correo electrónico (proporcionado por los administradores para emitirle una insignia).</li>
                <li><strong>Cookies y Tecnologías Similares:</strong> Cookies técnicas para gestionar la autenticación y mantener la seguridad de la sesión. No se recopila información con fines publicitarios ni de rastreo en sitios terceros.</li>
            </ul>

            <h3>3. Finalidades del Tratamiento</h3>
            <ul>
                <li><strong>Identidad Digital:</strong> Generar y asignar insignias digitales bajo el estándar <strong>Open Badges</strong>.</li>
                <li><strong>Notificaciones:</strong> Avisos por correo sobre nuevas insignias o actualizaciones de cuenta.</li>
                <li><strong>Verificación:</strong> Permitir que terceros validen la autenticidad de sus logros mediante una URL pública (siempre que la insignia sea pública).</li>
            </ul>

            <h3>4. Privacidad Técnica y Seguridad (Hashing)</h3>
            <ul>
                <li>El correo se procesa mediante una función criptográfica de un solo sentido (<strong>SHA-256</strong>) antes de inyectarse en los metadatos de la imagen y en el JSON-LD.</li>
                <li>Esto garantiza que su correo no sea legible en texto plano para sistemas externos, cumpliendo estándares internacionales de seguridad de credenciales.</li>
            </ul>

            <h3>5. Transferencia de Datos</h3>
            <p>No vendemos, alquilamos ni compartimos sus datos personales con terceros para fines comerciales.</p>
            <ul>
                <li><strong>OAuth2:</strong> La comunicación ocurre directamente entre el proveedor de identidad (Google) y el <strong>Host</strong>.</li>
                <li><strong>Acceso Técnico:</strong> El <strong>Host</strong> tiene acceso a la infraestructura con fines de mantenimiento, soporte y operación.</li>
            </ul>

            <h3>6. Derechos ARCO</h3>
            <p>Usted tiene derecho a conocer qué datos tenemos de usted, así como a solicitar su corrección o eliminación definitiva (<strong>Acceso, Rectificación, Cancelación y Oposición</strong>).</p>
            <ul>
                <li>Para ejercer estos derechos, envíe una solicitud al correo de contacto del <strong>Host</strong> o a través de los canales de soporte de la organización emisora.</li>
            </ul>
        </article>
    </div>
</template>
