<script setup>
import { getApiAdminBadgesUuid } from '~/services/admin-resource/admin-resource'
import { getApiV2IssuersIssuerUuidBadgesBadgeClassUuidAnalytics } from '~/services/issuer-analytics-resource/issuer-analytics-resource'

const route = useRoute()
const organizationId = route.params.id
const badgeId = route.params.badgeId

const { data: payload, status } = useLazyAsyncData(() => getApiAdminBadgesUuid(badgeId))

const { data: summary } = useLazyAsyncData(
    'badgeAnalytics-' + badgeId,
    () => getApiV2IssuersIssuerUuidBadgesBadgeClassUuidAnalytics(organizationId, badgeId),
    { transform: (data) => data.data },
)
</script>

<template>
    <div>
        <NuxtLink :to="`/organizations/${organizationId}`" class="link inline-flex items-center gap-1 text-sm mb-6">
            <Icon name="material-symbols:arrow-back" class="text-lg" />
            Volver a la organización
        </NuxtLink>

        <div v-if="status !== 'success'" class="flex flex-col gap-6">
            <div class="card card-pad space-y-3">
                <div class="skeleton h-8 w-1/2"></div>
                <div class="skeleton h-4 w-2/3"></div>
            </div>
            <div class="card overflow-hidden p-6">
                <div v-for="i in 4" :key="i" class="skeleton h-12 rounded-lg mb-3"></div>
            </div>
        </div>

        <template v-else>
            <!-- Badge header -->
            <div class="card card-pad relative overflow-hidden flex flex-col md:flex-row gap-6 items-center">
                <div aria-hidden="true" class="pointer-events-none absolute -top-20 -right-20 h-64 w-64 rounded-full bg-gold/15 blur-3xl"></div>
                <div class="rounded-[1.5rem] border border-line-strong bg-surface-2 p-2 shrink-0">
                    <img :src="payload.data.jsonPayload.image" :alt="payload.data.name" class="h-28 w-28 rounded-[calc(1.5rem-0.5rem)] object-cover" />
                </div>
                <div class="flex-1 flex flex-col gap-2 text-center md:text-left min-w-0">
                    <span class="badge badge-gold self-center md:self-start">
                        <Icon name="material-symbols:query-stats" class="text-base" />
                        Emisiones
                    </span>
                    <h1 class="font-display text-2xl md:text-3xl font-bold tracking-tight">{{ payload.data.name }}</h1>
                    <NuxtLink :to="`/organizations/${organizationId}`" class="inline-flex items-center gap-1.5 text-sm font-semibold text-teal hover:text-teal-strong self-center md:self-start">
                        <Icon name="material-symbols:domain" class="text-lg" />
                        {{ payload.data.issuer?.name }}
                    </NuxtLink>
                </div>
                <div class="flex gap-2 shrink-0">
                    <NuxtLink :to="`/badges/${badgeId}`" class="btn btn-outline btn-sm" aria-label="Ver credencial">
                        <Icon name="material-symbols:visibility" class="text-lg" />
                        Ver credencial
                    </NuxtLink>
                </div>
            </div>

            <!-- Analytics -->
            <div v-if="summary" class="mt-6 grid grid-cols-2 lg:grid-cols-4 gap-4">
                <div class="card card-pad flex flex-col gap-1">
                    <span class="text-xs text-ink-soft font-medium">Emitidas</span>
                    <span class="font-display text-3xl font-bold tabular">{{ summary.issued }}</span>
                </div>
                <div class="card card-pad flex flex-col gap-1">
                    <span class="text-xs text-ink-soft font-medium">Reclamadas</span>
                    <span class="font-display text-3xl font-bold tabular text-teal">{{ summary.claimed }}</span>
                </div>
                <div class="card card-pad flex flex-col gap-1">
                    <span class="text-xs text-ink-soft font-medium">Pendientes</span>
                    <span class="font-display text-3xl font-bold tabular text-warning">{{ summary.pending }}</span>
                </div>
                <div class="card card-pad flex flex-col gap-1">
                    <span class="text-xs text-ink-soft font-medium">Revocadas</span>
                    <span class="font-display text-3xl font-bold tabular text-danger">{{ summary.revoked }}</span>
                </div>
                <div class="card card-pad flex flex-col gap-2 lg:col-span-4">
                    <div class="flex items-center justify-between">
                        <span class="text-sm text-ink-soft font-medium">Tasa de reclamación</span>
                        <span class="text-sm font-bold tabular">{{ summary.claimRate?.toFixed(1) ?? '0.0' }}%</span>
                    </div>
                    <div class="h-2 rounded-full bg-surface-3 overflow-hidden">
                        <div class="h-full rounded-full bg-teal transition-all duration-500" :style="{ width: `${summary.claimRate ?? 0}%` }"></div>
                    </div>
                </div>
            </div>

            <!-- Recipients -->
            <div class="mt-6">
                <OrganizationsIssuanceRecipients :issuer-uuid="organizationId" :badge-class-uuid="badgeId" />
            </div>
        </template>
    </div>
</template>