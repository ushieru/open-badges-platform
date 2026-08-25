<script setup>
import { getApiV2Issuers } from '~/services/issuer-resource/issuer-resource';

definePageMeta({ title: 'Organizaciones' })

const { params, setParam } = useParams('getApiV2Issuers' + 'Params', { page: 1, sort: 'name' })
const { data: paginatedIssuers, status, refresh } = useLazyAsyncData(() => getApiV2Issuers(params.value),
    { transform: (data) => data.data })

const prevPage = _ => setParam('page', params.value.page - 1)
const nextPage = _ => setParam('page', params.value.page + 1)
watch(params, _ => refresh())
</script>

<template>
    <div>
        <div class="page-head flex-row items-end justify-between gap-4">
            <div>
                <h1 class="page-title">Organizaciones</h1>
                <p class="page-sub">Entidades emisoras registradas en la plataforma.</p>
            </div>
            <NuxtLink to="/organizations/new" class="btn btn-primary shrink-0">
                <Icon name="material-symbols:add" class="text-lg" />
                Nueva organización
            </NuxtLink>
        </div>

        <div class="card overflow-hidden">
            <div class="overflow-x-auto">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Organización</th>
                            <th class="hidden md:table-cell">Descripción</th>
                            <th>Contacto</th>
                            <th class="text-right">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-if="status !== 'success'">
                            <td colspan="4">
                                <div class="flex items-center justify-center gap-3 py-8">
                                    <span class="spinner"></span>
                                    <span class="text-sm text-ink-soft">Cargando organizaciones...</span>
                                </div>
                            </td>
                        </tr>
                        <tr v-else-if="!paginatedIssuers?.data?.length">
                            <td colspan="4">
                                <div class="empty">
                                    <span class="flex h-16 w-16 items-center justify-center rounded-2xl bg-teal-soft text-teal">
                                        <Icon name="material-symbols:domain" class="text-3xl" />
                                    </span>
                                    <h3 class="font-display text-lg font-bold text-ink">Sin organizaciones</h3>
                                    <p class="text-sm">Aún no hay organizaciones registradas.</p>
                                </div>
                            </td>
                        </tr>
                        <tr v-else v-for="issuer in paginatedIssuers?.data" :key="issuer.id">
                            <td>
                                <div class="flex gap-3 items-center">
                                    <div class="rounded-xl border border-line-strong bg-surface-2 p-1 shrink-0">
                                        <img :src="issuer.logoUrl" :alt="`Logo de ${issuer.name}`" class="h-11 w-11 rounded-lg object-cover" />
                                    </div>
                                    <div class="flex flex-col min-w-0">
                                        <p class="font-semibold truncate">{{ issuer.name }}</p>
                                        <NuxtLink :to="issuer.url" target="_blank" rel="noopener" class="text-xs text-ink-soft hover:text-teal truncate">
                                            {{ issuer.url }}
                                        </NuxtLink>
                                    </div>
                                </div>
                            </td>
                            <td class="hidden md:table-cell text-ink-soft">
                                <span class="line-clamp-2 max-w-xs">{{ issuer.description }}</span>
                            </td>
                            <td>
                                <button class="link text-sm" @click="copy(issuer.email, 'Email copiado')">{{ issuer.email }}</button>
                            </td>
                            <td class="text-right">
                                <NuxtLink :to="`/organizations/${issuer.id}`" class="btn btn-outline btn-sm" aria-label="Configurar organización">
                                    <Icon name="material-symbols:settings" class="text-lg" />
                                </NuxtLink>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="border-t border-line p-4">
                <AppPagination :meta="paginatedIssuers?.meta" @prev="prevPage" @next="nextPage" />
            </div>
        </div>
    </div>
</template>
