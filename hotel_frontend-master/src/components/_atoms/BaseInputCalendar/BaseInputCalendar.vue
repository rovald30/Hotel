<script setup>
defineProps({
  label: {
    type: [String, Boolean],
    default: false,
  },
  modelValue: {
    type: String,
    default: "",
  },
  inputType: {
    type: String,
    default: "text",
  },
  errorMsg: String,
});
</script>

<template>
  <div data-testid="base-input-calendar" class="identity-input mb-4">
    <label v-if="label" class="block text-gray-700 text-sm font-bold mb-2">
      {{ label }}
    </label>
    <input
      class="flex relative px-6 shadow appearance-none borderrounded py-2 text-gray-700 mb-1.5 leading-tight focus:outline-none focus:shadow-outline"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
      v-bind="$attrs"
      :type="inputType"
      :class="{ 'border border-red-500': errorMsg }"
    />
    <span class="flex absolute text-xs text-red-500" id="errorMsg">{{
      errorMsg
    }}</span>
  </div>
</template>

<style scoped></style>
