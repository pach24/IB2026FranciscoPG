package com.iberdrola.practicas2026.FranciscoPG.domain.analytics

object AnalyticsEvent {

    // adb shell setprop debug.firebase.analytics.app com.iberdrola.practicas2026.FranciscoPG
    // adb shell setprop debug.firebase.analytics.app .none.


    // ── Pantalla Home ────────────────────────────────────────────────────────
    const val TAP_MY_INVOICES_CARD = "tap_my_invoices_card"
    const val TAP_ELECTRONIC_INVOICE_CARD = "tap_electronic_invoice_card"
    const val TAP_LATEST_INVOICE_CARD = "tap_latest_invoice_card"
    const val TAP_FORCE_CRASH = "tap_force_crash"

    // ── Pantalla Mis Facturas ────────────────────────────────────────────────
    const val TAP_TAB_LUZ = "tap_tab_luz"
    const val TAP_TAB_GAS = "tap_tab_gas"
    const val TAP_OPEN_FILTERS = "tap_open_filters"
    const val PULL_TO_REFRESH = "pull_to_refresh"
    const val FEEDBACK_RATED = "feedback_rated"
    const val FEEDBACK_LATER = "feedback_later"
    const val FEEDBACK_DISMISSED = "feedback_dismissed"

    // ── Pantalla Filtros ─────────────────────────────────────────────────────
    const val FILTER_TAP_START_DATE = "filter_tap_start_date"
    const val FILTER_TAP_END_DATE = "filter_tap_end_date"
    const val FILTER_RANGE_SLIDER_CHANGED = "filter_range_slider_changed"
    const val FILTER_TAP_STATUS_CHECKBOX = "filter_tap_status_checkbox"
    const val APPLY_FILTERS = "apply_filters"
    const val CLEAR_FILTERS = "clear_filters"

    // ── Pantalla Factura Electrónica ─────────────────────────────────────────
    const val TAP_CONTRACT = "tap_contract"

    // ── Wizard activar factura electrónica ───────────────────────────────────
    // Página email
    const val ACTIVATE_WIZARD_START = "activate_wizard_start"
    const val ACTIVATE_WIZARD_TAP_EMAIL_FIELD = "activate_wizard_tap_email_field"
    const val ACTIVATE_WIZARD_TAP_LEGAL_CHECKBOX = "activate_wizard_tap_legal_checkbox"
    const val ACTIVATE_WIZARD_TAP_CONDITIONS_LINK = "activate_wizard_tap_conditions_link"
    const val ACTIVATE_WIZARD_TAP_MORE_INFO = "activate_wizard_tap_more_info"
    const val ACTIVATE_WIZARD_TAP_NEXT = "activate_wizard_tap_next"
    // Página OTP
    const val ACTIVATE_WIZARD_TAP_OTP_FIELD = "activate_wizard_tap_otp_field"
    const val ACTIVATE_WIZARD_RESEND_CODE = "activate_wizard_resend_code"
    const val ACTIVATE_WIZARD_TAP_CONFIRM = "activate_wizard_tap_confirm"
    // Resultado
    const val ACTIVATE_WIZARD_COMPLETE = "activate_wizard_complete"
    const val ACTIVATE_WIZARD_ABANDON = "activate_wizard_abandon"

    // ── Pantalla modificar email ─────────────────────────────────────────────
    const val TAP_MODIFY_EMAIL = "tap_modify_email"

    // ── Wizard modificar email ───────────────────────────────────────────────
    // Página nuevo email
    const val MODIFY_WIZARD_TAP_EMAIL_FIELD = "modify_wizard_tap_email_field"
    const val MODIFY_WIZARD_TAP_NEXT = "modify_wizard_tap_next"
    // Página OTP
    const val MODIFY_WIZARD_TAP_OTP_FIELD = "modify_wizard_tap_otp_field"
    const val MODIFY_WIZARD_RESEND_CODE = "modify_wizard_resend_code"
    const val MODIFY_WIZARD_TAP_CONFIRM = "modify_wizard_tap_confirm"
    // Resultado
    const val MODIFY_WIZARD_COMPLETE = "modify_email_wizard_complete"
    const val MODIFY_WIZARD_ABANDON = "modify_email_wizard_abandon"

    // ── Claves de parámetros ─────────────────────────────────────────────────
    const val PARAM_SUPPLY_TYPE = "supply_type"
    const val PARAM_FILTER_COUNT = "filter_count"
    const val PARAM_STATUS = "status"
    const val PARAM_CHECKED = "checked"
    const val PARAM_MIN_AMOUNT = "min_amount"
    const val PARAM_MAX_AMOUNT = "max_amount"
    const val PARAM_FLOW = "flow"

    // ── Nombres de pantallas (screen_view) ───────────────────────────────────
    const val SCREEN_HOME = "home"
    const val SCREEN_MY_INVOICES = "my_invoices"
    const val SCREEN_INVOICE_FILTER = "invoice_filter"
    const val SCREEN_ELECTRONIC_INVOICE = "electronic_invoice"
    const val SCREEN_ACTIVATE_INVOICE = "activate_invoice"
    const val SCREEN_MODIFY_EMAIL = "modify_email"
    const val SCREEN_MODIFY_EMAIL_WIZARD = "modify_email_wizard"
}
