<script setup>
import { getApiV2IssuersIssuerUuidBadgesAnalytics } from '~/services/issuer-analytics-resource/issuer-analytics-resource'

const props = defineProps({
    issuerUuid: { type: String, required: true },
})

const { params, setParam } = useParams('issuanceSummary' + props.issuerUuid + 'Params', { page: 1, size: 10 })
const { data: paginated, status, refresh } = useLazyAsyncData(
    () => getApiV2IssuersIssuerUuidBadgesAnalytics(props.issuerUuid, params.value),
    { transform: (data) => data.data },
)

const prevPage = () => setParam('page', params.value.page - 1)
const nextPage = () => setParam('page', params.value.page + 1)
watch(params, () => refresh())
</script>

<template>
    <div class="card overflow-hidden">
        <div class="card-pad pb-4">
            <h2 class="font-display text-lg font-bold">Emisiones</h2>
            <p class="text-sm text-ink-soft">Resumen de credenciales emitidas y su estado de reclamación.</p>
        </div>

        <div v-if="status !== 'success'" class="px-6 pb-6">
            <div class="space-y-3">
                <div v-for="i in 4" :key="i" class="skeleton h-10 rounded-lg"></div>
            </div>
        </div>

        <div v-else-if="!paginated?.data?.length" class="empty">
            <span class="flex h-14 w-14 items-center justify-center rounded-2xl bg-teal-soft text-teal">
                <Icon name="material-symbols:query-stats" class="text-2xl" />
            </span>
            <p class="text-sm">Aún no hay emisiones registradas.</p>
        </div>

        <template v-else>
            <div class="overflow-x-auto">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Credencial</th>
                            <th class="text-center">Emitidas</th>
                            <th class="text-center">Reclamadas</th>
                            <th class="text-center">Pendientes</th>
                            <th class="text-center">Revocadas</th>
                            <th class="w-40">% Reclamación</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="item in paginated.data" :key="item.badgeId">
                            <td>
                                <NuxtLink :to="`/organizations/${issuerUuid}/badges/${item.badgeId}/emissions`"
                                    class="inline-flex items-center gap-3 hover:text-teal transition-colors">
                                    <div class="rounded-lg border border-line-strong bg-surface-2 p-0.5 shrink-0">
                                        <img v-if="item.imageUrl" :src="item.imageUrl" :alt="item.name" class="h-9 w-9 rounded-md object-cover" />
                                    </div>
                                    <span class="font-semibold">{{ item.name }}</span>
                                </NuxtLink>
                            </td>
                            <td class="text-center tabular font-semibold">{{ item.issued }}</td>
                            <td class="text-center tabular">{{ item.claimed }}</td>
                            <td class="text-center tabular text-warning">{{ item.pending }}</td>
                            <td class="text-center tabular text-danger">{{ item.revoked }}</td>
                            <td>
                                <div class="flex items-center gap-2">
                                    <div class="flex-1 h-2 rounded-full bg-surface-3 overflow-hidden">
                                        <div class="h-full rounded-full bg-teal transition-all duration-500"
                                            :style="{ width: `${item.claimRate ?? 0}%` }"></div>
                                    </div>
                                    <span class="text-xs tabular font-semibold">{{ item.claimRate?.toFixed(1) ?? '0.0' }}%</span>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="border-t border-line p-4">
                <AppPagination :meta="paginated?.meta" @prev="prevPage" @next="nextPage" />
            </div>
        </template>
    </div>
</template>