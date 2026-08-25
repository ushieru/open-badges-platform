const STORAGE_KEY = 'obp-theme'

export const useTheme = () => {
    const isDark = useState<boolean>('obp-theme', () => false)

    const apply = (dark: boolean) => {
        isDark.value = dark
        document.documentElement.classList.toggle('dark', dark)
        try { localStorage.setItem(STORAGE_KEY, dark ? 'dark' : 'light') } catch { /* noop */ }
    }

    const init = () => {
        let dark = false
        try {
            const stored = localStorage.getItem(STORAGE_KEY)
            dark = stored
                ? stored === 'dark'
                : window.matchMedia('(prefers-color-scheme: dark)').matches
        } catch { /* noop */ }
        apply(dark)
    }

    const toggle = () => apply(!isDark.value)

    return { isDark, init, toggle }
}
