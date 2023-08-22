<script setup>
import { onMounted, ref, watch } from "vue";
import IconAscSort from "@/components/icons/IconAscSort.vue";
import IconDescSort from "@/components/icons/IconDescSort.vue";
import ModalWindow from "@/components/_atoms/ModalWindow/ModalWindow.vue";
import { useModalWindowStore } from "@/stores/modal-window.js";
import BaseSelector from "@/components/_atoms/BaseSelector/BaseSelector.vue";
import TrueIcon from "~icons/flat-color-icons/ok";
import FalseIcon from "~icons/flat-color-icons/high-priority";
import LoadingDataIcon from "~icons/line-md/loading-twotone-loop";

const props = defineProps({
  tableData: Array,
  tableHeaders: Array,
  showIndexes: Boolean,
  currentPage: Number,
  itemsPerPage: Number,
  tableKey: Number,
  editable: Boolean,
  removable: Boolean,
  types: Array,
  booleanSelectorFor: Array,
  notAllowToChange: Array,
});

const date = ref("");

const emits = defineEmits([
  "search",
  "update:inputSearch",
  "orderBy",
  "removeEntity",
  "updateEntity",
  "updateType",
]);

const isLoading = ref(true);
const copyTableData = ref([]);
const arrSortByAsc = ref([]);

onMounted(() => {
  setTimeout(() => {
    isLoading.value = false;
  }, 2000);
});
copyTableData.value.push(...props.tableData);

copyTableData.value.forEach((obj) => {
  Object.entries(obj)
    .filter(([, value]) => value === null)
    .forEach(([key]) => (obj[key] = ""));
  arrSortByAsc.value.push(true);
});

const getPropertyName = (keyFromInput) => {
  return props.tableData.length !== 0
    ? Object.keys(props.tableData[0])[keyFromInput]
    : null;
};

const inputSearch = ref("");
const sortBy = ref("");
const updateInput = (key, event) => {
  sortBy.value = getPropertyName(key);
  inputSearch.value = event.target.value;
  emits("update:inputSearch", event.target.value);
};

const updateDate = (key) => {
  if (date.value !== "Invalid Date") {
    inputSearch.value = new Date(Date.parse(date.value)).toLocaleDateString();
    sortBy.value = getPropertyName(key);
  }

  emits("update:inputSearch", date.value);
};

const updateType = (key, event) => {
  sortBy.value = getPropertyName(key);
  emits("updateType", sortBy.value, event.target.value);
};

watch(inputSearch, () => emits("search", [sortBy.value, inputSearch.value]));

const format = (date) => {
  const day = date.getDay() < 10 ? "0".concat(date.getDay()) : date.getDay();
  const month =
    date.getMonth() + 1 < 10
      ? "0".concat(date.getMonth() + 1)
      : date.getMonth() + 1;
  const year = date.getFullYear();

  return `${year}-${month}-${day}`;
};

const changeOrderBy = (key) => {
  arrSortByAsc.value[key]
    ? (arrSortByAsc.value[key] = false)
    : (arrSortByAsc.value[key] = true);

  emits("orderBy", getPropertyName(key));
};

const modalWindowStore = useModalWindowStore();
const slotContent = ref("");

const isEdit = ref(false);
const isRemove = ref(false);
const isButtonDisable = ref(false);
const entity = ref({});

const editModal = (id, object) => {
  isEdit.value = true;
  modalWindowStore.showModal = true;

  slotContent.value = object;
  entity.value = Object.assign({}, object);
};

const removeModal = (id, object) => {
  isRemove.value = true;
  modalWindowStore.showModal = true;

  slotContent.value = object;
  entity.value = object;
};

const updateEntity = () => {
  isButtonDisable.value = true;
  setTimeout(() => {
    emits("updateEntity", entity.value);
    modalWindowStore.showModal = false;
    isRemove.value = false;
  }, 2000);
};

const deleteEntity = () => {
  isButtonDisable.value = true;
  setTimeout(() => {
    emits("removeEntity", entity.value["id"]);
    modalWindowStore.showModal = false;
    isRemove.value = false;
  }, 2000);
};
</script>

<template>
  <ModalWindow>
    <div id="edit" v-if="isEdit" class="grid gap-6 mb-6 md:grid-cols-2">
      <div class="" v-for="(value, key) in slotContent" :key="value">
        <div>
          <label
            :for="key"
            class="block mb-2 text-sm font-medium text-gray-900"
            >{{ key }}</label
          >
          <BaseSelector
            :options="['', 'true', 'false']"
            :selected-option="value.toString()"
            v-if="value === true || value === false"
            :selector-styles="`bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2`"
            @change="(event) => (entity[key] = event.target.value)"
          />
          <BaseSelector
            v-else-if="key.includes('Type')"
            :selected-option="value"
            :options="types"
            :selector-styles="`bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-1.5`"
            @change="(event) => (entity[key] = event.target.value)"
          />
          <input
            v-else
            :disabled="notAllowToChange && notAllowToChange.includes(key)"
            type="text"
            v-model="entity[key]"
            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 disabled:text-gray-500 disabled:bg-gray-100"
          />
        </div>
      </div>

      <div class="flex justify-end pt-6">
        <button
          @click="updateEntity"
          type="button"
          :disabled="isButtonDisable"
          class="focus:outline-none text-white bg-yellow-500 hover:bg-yellow-400 focus:ring-4 focus:ring-yellow-300 font-medium rounded-lg text-sm px-5 py-2.5"
        >
          Update
        </button>
      </div>
    </div>

    <div id="remove" v-else-if="isRemove">
      <label class="font-bold text-2xl mb-2"
        >Are you sure that you want delete this?</label
      >
      <hr />
      <div class="grid gap-2 mt-6 md:grid-cols-2">
        <div v-for="(value, key) in slotContent" :key="key">
          <div>
            <p class="font-bold">{{ key }}:</p>
            {{ value }}
          </div>
        </div>
      </div>

      <div class="flex justify-end">
        <button
            @click="deleteEntity"
            type="button"
            :disabled="isButtonDisable"
            class="right-0 disabled: focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2"
        >
          Delete
        </button>
      </div>
    </div>
  </ModalWindow>

  <!---  TABLE  --->
  <div class="m-20 mr-40">
    <table class="table w-full items-center border-collapse border shadow-2xl">
      <thead class="bg-blue-300/50">
        <tr>
          <td v-if="showIndexes" class="px-4"></td>
          <td class="pt-4" v-for="(header, key) in tableHeaders" :key="key">
            <p
              @click.prevent="changeOrderBy(key)"
              class="flex cursor-pointer pl-8"
            >
              <span class="pt-0.5 pr-0.5">
                <IconAscSort
                  class="h-5 w-5"
                  :id="key"
                  v-if="arrSortByAsc[key]"
                />
                <IconDescSort v-else class="h-5 w-5" :id="key" />
              </span>
              {{ header }}
            </p>
          </td>
        </tr>
        <tr>
          <td v-if="showIndexes" class="font-bold px-4">#</td>
          <td class="px-4 p-3" v-for="(header, key) in tableHeaders" :key="key">
            <BaseSelector
              v-if="header.includes('Type')"
              :options="types"
              class="pb-2 -m-1"
              @change="updateType(key, $event)"
            />
            <BaseSelector
              class="mb-2 ml-2 w-32"
              :options="['', true, false]"
              v-else-if="
                booleanSelectorFor && booleanSelectorFor.includes(header)
              "
              @change="updateType(key, $event)"
            />

            <Datepicker
              v-else-if="header.includes('Date')"
              v-model="date"
              :format="format"
              :id="key"
              class="w-40"
              :enable-time-picker="false"
              @update:model-value="updateDate(key)"
            />

            <input
              v-else-if="tableHeaders"
              type="text"
              class="block w-40 rounded h-7 border-solid border border-black/10 hover:border-black/30"
              :id="key"
              @input="updateInput(key, $event)"
            />
          </td>
        </tr>
      </thead>
      <template v-if="isLoading">
        <tbody>
          <tr>
            <td
              :colspan="
                showIndexes ? tableHeaders.length + 1 : tableHeaders.length
              "
              class="py-6"
              align="center"
            >
              <LoadingDataIcon style="font-size: 3em" />
            </td>
          </tr>
        </tbody>
      </template>
      <template v-else-if="tableData.length === 0">
        <tbody>
          <tr>
            <td
              :colspan="
                showIndexes ? tableHeaders.length + 1 : tableHeaders.length
              "
              class="py-10"
              align="center"
            >
              <p>No data</p>
            </td>
          </tr>
        </tbody>
      </template>
      <template v-else>
        <tbody
          class="group bg-gray-300/50 border-t-2 border-black/30 justify-center place-items-center"
          v-for="(object, id) in tableData"
          :key="object"
        >
          <div
            v-if="props.editable || props.removable"
            class="absolute w-full h-full ml-36"
          >
            <div :id="id" class="hidden group-hover:block absolute right-0">
              <button
                v-if="props.editable"
                @click="editModal(id, object)"
                class="text-yellow-700 hover:text-white border border-yellow-700 hover:bg-yellow-500 focus:ring-4 focus:outline-none focus:ring-yellow-100 font-medium rounded-lg text-sm px-2.5 py-1 text-center mr-2"
              >
                Edit
              </button>
              <button
                v-if="props.removable"
                @click="removeModal(id, object)"
                class="text-red-700 hover:text-white border border-red-700 hover:bg-red-500 focus:ring-4 focus:outline-none focus:ring-red-100 font-medium rounded-lg text-sm px-2.5 py-1 text-center mr-2"
              >
                Delete
              </button>
            </div>
          </div>
          <tr class="hover:bg-gray-300">
            <td v-if="showIndexes" class="px-4">
              {{ (currentPage - 1) * itemsPerPage + id + 1 }}
            </td>
            <td
              class="px-6 py-1 pl-10 mr-2"
              v-for="element in object"
              :key="element"
            >
              <p class="pl-6" v-if="typeof element === 'boolean'">
                <TrueIcon v-if="element === true" />
                <FalseIcon v-else-if="element === false" />
              </p>
              <p v-else>{{ element }}</p>
            </td>
          </tr>
        </tbody>
      </template>
    </table>
  </div>
</template>

<style lang="scss">
.dp__input {
  height: 1.9rem;
  border-color: black;
}
</style>
