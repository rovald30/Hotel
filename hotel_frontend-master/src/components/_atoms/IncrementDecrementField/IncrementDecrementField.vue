<script setup>
defineProps({
  labelText: String,
  buttonStyle: String,
  amount: {
    type: Number,
    default: 0,
  },
});

defineEmits(["increase", "decrease"]);
</script>

<template>
  <div v-bind="$attrs">
    <label class="block text-gray-700 text-sm font-bold mb-2">
      {{ labelText }}
    </label>
    <div class="flex bg-white border border-gray-300 shadow h-10">
      <button
        @click="$emit('decrease')"
        class="pl-4 pr-4 w-fit bg-blue-400 hover:bg-blue-600 hover:text-white"
        :class="buttonStyle"
      >
        -
      </button>
      <p class="flex w-full justify-center items-center">{{ amount }}</p>
      <button
        @click="$emit('increase')"
        class="pr-3.5 pl-3.5 w-fit bg-blue-400 hover:bg-blue-600 hover:text-white"
        :class="buttonStyle"
      >
        +
      </button>
    </div>
  </div>
</template>
