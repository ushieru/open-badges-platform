<script setup>
defineProps({
    size: { type: Number, default: 36 },
    withWordmark: { type: Boolean, default: false },
})
</script>

<template>
    <span v-if="withWordmark" class="inline-flex items-center gap-3">
        <img src="/brand/logo-mark.svg" :width="size" :height="size" :alt="`Open Badges Platform logo`" class="drop-shadow-sm" />
        <span class="flex flex-col leading-none">
            <span class="font-display font-bold text-lg tracking-tight text-ink">Open Badges</span>
            <span class="text-[0.6rem] font-semibold tracking-[0.3em] text-teal uppercase">Platform</span>
        </span>
    </span>
    <img v-else src="/brand/logo-mark.svg" :width="size" :height="size" :alt="`Open Badges Platform logo`" class="drop-shadow-sm" />
</template>
