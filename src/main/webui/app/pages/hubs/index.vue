<script setup>
const { me } = useAuth()

onMounted(() => {
    if (me.value?.memberships.length === 1)
        navigateTo(`/organizations/${me.value.memberships[0].issuer.id}`)
})
</script>

<template>
    <div>
        <div class="page-head">
            <h1 class="page-title">Mis organizaciones</h1>
            <p class="page-sub">Entidades en las que participas como miembro.</p>
        </div>

        <div v-if="!me?.memberships?.length" class="card">
            <div class="empty">
                <span class="flex h-16 w-16 items-center justify-center rounded-2xl bg-teal-soft text-teal">
                    <Icon name="material-symbols:hub" class="text-3xl" />
                </span>
                <h3 class="font-display text-lg font-bold text-ink">Aún no formas parte de una organización</h3>
                <p class="max-w-sm text-sm">Cuando una organización te invite como miembro, aparecerá aquí.</p>
            </div>
        </div>

        <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
            <NuxtLink v-for="membership in me?.memberships" :key="membership.id"
                :to="`/organizations/${membership.issuer.id}`" class="card card-pad card-hover group flex flex-col gap-5">
                <div class="flex items-center gap-4">
                    <div class="rounded-xl border border-line-strong bg-surface-2 p-1.5 shrink-0">
                        <img :src="membership.issuer.logoUrl" :alt="`Logo de ${membership.issuer.name}`" class="h-14 w-14 rounded-lg object-cover" />
                    </div>
                    <div class="min-w-0 flex-1">
                        <h2 class="font-display text-lg font-bold line-clamp-1">{{ membership.issuer.name }}</h2>
                        <span class="badge badge-teal mt-1">{{ membership.role }}</span>
                    </div>
                    <Icon name="material-symbols:arrow-forward" class="text-xl text-ink-soft transition-transform group-hover:translate-x-1 group-hover:text-teal" />
                </div>
            </NuxtLink>
        </div>
    </div>
</template>
