<script setup>
import json from '~/assets/json/json-dl-assertion-example.json'
const { me } = useAuth()
</script>

<template>
    <div class="flex flex-col gap-24 md:gap-32">
        <!-- Hero -->
        <section class="relative overflow-hidden rounded-[2.5rem] border border-line bg-surface-2 px-6 py-20 md:px-16 md:py-28">
            <!-- Ambient gradient orbs -->
            <div aria-hidden="true" class="pointer-events-none absolute inset-0 overflow-hidden rounded-[2.5rem]">
                <div class="absolute -top-24 -right-24 h-96 w-96 rounded-full bg-teal/15 blur-3xl"></div>
                <div class="absolute -bottom-32 -left-24 h-96 w-96 rounded-full bg-gold/15 blur-3xl"></div>
                <div class="absolute inset-0 opacity-[0.4]"
                    style="background-image: radial-gradient(circle at 1px 1px, color-mix(in srgb, var(--ink) 8%, transparent) 1px, transparent 0); background-size: 28px 28px;"></div>
            </div>

            <div class="relative mx-auto max-w-3xl text-center">
                <span class="badge badge-teal mb-8">
                    <Icon name="material-symbols:verified" class="text-base" />
                    Estándar Open Badges 2.0 · 1EdTech
                </span>
                <h1 class="font-display text-4xl font-bold tracking-tight text-balance sm:text-6xl md:text-7xl">
                    Confianza para tus
                    <span class="text-gradient">credenciales digitales</span>
                </h1>
                <p class="mx-auto mt-6 max-w-2xl text-lg text-ink-soft text-balance">
                    Emisión, verificación y gestión de insignias digitales soberanas e inmutables,
                    para organizaciones e instituciones que demandan máxima integridad de datos.
                </p>
                <div class="mt-10 flex flex-col sm:flex-row items-center justify-center gap-3">
                    <NuxtLink v-if="!me" to="/login" class="btn btn-primary btn-lg rounded-full px-8">
                        Iniciar sesión
                        <span class="flex h-7 w-7 items-center justify-center rounded-full bg-white/20">
                            <Icon name="material-symbols:arrow-forward" class="text-base" />
                        </span>
                    </NuxtLink>
                    <NuxtLink v-else to="/badges" class="btn btn-primary btn-lg rounded-full px-8">
                        Mis credenciales
                        <span class="flex h-7 w-7 items-center justify-center rounded-full bg-white/20">
                            <Icon name="material-symbols:arrow-forward" class="text-base" />
                        </span>
                    </NuxtLink>
                    <a href="#features" class="btn btn-outline btn-lg rounded-full px-8">Conocer la plataforma</a>
                </div>
            </div>
        </section>

        <!-- Pillars (asymmetric bento) -->
        <section id="features" class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div class="card card-pad card-hover md:col-span-1 flex flex-col gap-4">
                <span class="flex h-12 w-12 items-center justify-center rounded-2xl bg-teal-soft text-teal">
                    <Icon name="material-symbols:lock" class="text-2xl" />
                </span>
                <h3 class="font-display text-xl font-bold">Seguridad y cumplimiento</h3>
                <p class="text-sm text-ink-soft leading-relaxed">
                    Metadatos inyectados (baking) y hashing SHA-256. Cada insignia es una prueba inmutable, segura y soberana de tus logros.
                </p>
            </div>
            <div class="card card-pad card-hover md:col-span-2 flex flex-col gap-4 bg-gradient-to-br from-[#0b1b33] to-[#0e7490] text-white">
                <span class="flex h-12 w-12 items-center justify-center rounded-2xl bg-white/15">
                    <Icon name="material-symbols:language" class="text-2xl" />
                </span>
                <h3 class="font-display text-xl font-bold">Interoperabilidad global</h3>
                <p class="text-sm leading-relaxed opacity-90">
                    Tus insignias no están atrapadas: son portátiles y compatibles con backpacks globales,
                    LinkedIn y cualquier plataforma de verificación W3C.
                </p>
            </div>
            <div class="card card-pad card-hover md:col-span-2 flex flex-col gap-4">
                <span class="flex h-12 w-12 items-center justify-center rounded-2xl bg-gold-soft text-gold-strong">
                    <Icon name="material-symbols:verified" class="text-2xl" />
                </span>
                <h3 class="font-display text-xl font-bold">Validación en tiempo real</h3>
                <p class="text-sm text-ink-soft leading-relaxed">
                    Cualquier tercero confirma la legitimidad de tus logros al instante. Transforma certificados estáticos en activos digitales dinámicos.
                </p>
            </div>
            <div class="card card-pad card-hover md:col-span-1 flex flex-col gap-4">
                <span class="flex h-12 w-12 items-center justify-center rounded-2xl bg-teal-soft text-teal">
                    <Icon name="material-symbols:shield" class="text-2xl" />
                </span>
                <h3 class="font-display text-xl font-bold">Autenticidad portable</h3>
                <p class="text-sm text-ink-soft leading-relaxed">
                    Validación criptográfica que reside en la propia insignia, verificable sin intermediarios.
                </p>
            </div>
        </section>

        <!-- Code / JSON-LD -->
        <section class="grid grid-cols-1 lg:grid-cols-2 gap-10 items-center">
            <div class="flex flex-col gap-6">
                <span class="badge badge-neutral">
                    <Icon name="material-symbols:code-blocks" class="text-base" />
                    Metadata verificable
                </span>
                <h2 class="font-display text-3xl md:text-4xl font-bold tracking-tight text-balance">
                    Datos vinculados, legibles por máquinas
                </h2>
                <p class="text-ink-soft text-balance">
                    Implementamos JSON-LD y Linked Data para que la información del logro sea indexada y
                    reconocida globalmente de forma inmediata.
                </p>
                <div class="flex items-center gap-3">
                    <span class="flex h-10 w-10 items-center justify-center rounded-xl bg-teal-soft text-teal">
                        <Icon name="material-symbols:database" class="text-xl" />
                    </span>
                    <span class="font-mono text-xs text-ink-soft">JSON-LD · SHA-256 · Open Badges</span>
                </div>
            </div>

            <div class="rounded-[2rem] border border-line-strong bg-surface-2 p-1.5 shadow-card-hover">
                <div class="rounded-[calc(2rem-0.375rem)] overflow-hidden bg-[#071223]">
                    <div class="flex items-center justify-between px-5 py-3 border-b border-white/10">
                        <div class="flex gap-1.5" aria-hidden="true">
                            <span class="h-3 w-3 rounded-full bg-danger/80"></span>
                            <span class="h-3 w-3 rounded-full bg-gold/80"></span>
                            <span class="h-3 w-3 rounded-full bg-success/80"></span>
                        </div>
                        <span class="font-mono text-[10px] uppercase tracking-widest text-white/50">Open Badges</span>
                    </div>
                    <pre class="overflow-x-auto p-6 font-mono text-xs leading-relaxed text-[#7dd3fc]"><code>{{ json }}</code></pre>
                </div>
            </div>
        </section>
    </div>
</template>
