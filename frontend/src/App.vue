<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

const messages = ref([])
const stats = ref({ totalMessages: 0, uniqueIds: 0, topIds: [] })
const connected = ref(false)
const paused = ref(false)
const filter = ref('')
const maxMessages = 100

let ws = null

const filteredMessages = computed(() => {
  if (!filter.value) return messages.value
  return messages.value.filter(m => 
    m.idHex.toLowerCase().includes(filter.value.toLowerCase()) ||
    m.ecuName.toLowerCase().includes(filter.value.toLowerCase())
  )
})

function connect() {
  ws = new WebSocket('ws://localhost:8080/ws/can')
  
  ws.onopen = () => {
    connected.value = true
  }
  
  ws.onclose = () => {
    connected.value = false
    setTimeout(connect, 2000)
  }
  
  ws.onmessage = (event) => {
    if (paused.value) return
    const msg = JSON.parse(event.data)
    messages.value.unshift(msg)
    if (messages.value.length > maxMessages) {
      messages.value.pop()
    }
  }
}

async function fetchStats() {
  try {
    const res = await fetch('http://localhost:8080/api/stats')
    stats.value = await res.json()
  } catch (e) {}
}

function clearMessages() {
  messages.value = []
}

function getEcuColor(ecuName) {
  const colors = {
    'Engine Control': 'text-red-400',
    'Transmission': 'text-blue-400',
    'ABS/ESP': 'text-yellow-400',
    'Instrument Cluster': 'text-green-400',
    'Body Control': 'text-purple-400'
  }
  return colors[ecuName] || 'text-gray-400'
}

onMounted(() => {
  connect()
  setInterval(fetchStats, 1000)
})

onUnmounted(() => {
  if (ws) ws.close()
})
</script>

<template>
  <div class="min-h-screen bg-gray-900 text-white p-6">
    <header class="flex items-center justify-between mb-6">
      <div>
        <h1 class="text-2xl font-bold">CAN Bus Analyzer</h1>
        <p class="text-gray-400 text-sm">Real-time message visualization</p>
      </div>
      <div class="flex items-center gap-4">
        <span :class="connected ? 'text-green-400' : 'text-red-400'" class="flex items-center gap-2">
          <span class="w-2 h-2 rounded-full" :class="connected ? 'bg-green-400' : 'bg-red-400'"></span>
          {{ connected ? 'Connected' : 'Disconnected' }}
        </span>
      </div>
    </header>

    <div class="grid grid-cols-4 gap-4 mb-6">
      <div class="bg-gray-800 rounded-lg p-4">
        <p class="text-gray-400 text-sm">Total Messages</p>
        <p class="text-2xl font-bold">{{ stats.totalMessages?.toLocaleString() }}</p>
      </div>
      <div class="bg-gray-800 rounded-lg p-4">
        <p class="text-gray-400 text-sm">Unique IDs</p>
        <p class="text-2xl font-bold">{{ stats.uniqueIds }}</p>
      </div>
      <div class="bg-gray-800 rounded-lg p-4">
        <p class="text-gray-400 text-sm">Buffer Size</p>
        <p class="text-2xl font-bold">{{ messages.length }}</p>
      </div>
      <div class="bg-gray-800 rounded-lg p-4">
        <p class="text-gray-400 text-sm">Status</p>
        <p class="text-2xl font-bold" :class="paused ? 'text-yellow-400' : 'text-green-400'">
          {{ paused ? 'Paused' : 'Live' }}
        </p>
      </div>
    </div>

    <div class="flex gap-4 mb-4">
      <input 
        v-model="filter"
        type="text" 
        placeholder="Filter by ID or ECU..."
        class="flex-1 bg-gray-800 rounded-lg px-4 py-2 text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500"
      />
      <button 
        @click="paused = !paused"
        :class="paused ? 'bg-green-600 hover:bg-green-700' : 'bg-yellow-600 hover:bg-yellow-700'"
        class="px-4 py-2 rounded-lg font-medium"
      >
        {{ paused ? 'Resume' : 'Pause' }}
      </button>
      <button 
        @click="clearMessages"
        class="px-4 py-2 bg-red-600 hover:bg-red-700 rounded-lg font-medium"
      >
        Clear
      </button>
    </div>

    <div class="bg-gray-800 rounded-lg overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-700">
          <tr>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-300">Time</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-300">ID</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-300">ECU</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-300">DLC</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-300">Data</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-300">Signal</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-700">
          <tr v-for="msg in filteredMessages" :key="msg.timestamp + msg.id" class="hover:bg-gray-750">
            <td class="px-4 py-2 text-sm font-mono text-gray-400">
              {{ new Date(msg.timestamp).toLocaleTimeString() }}
            </td>
            <td class="px-4 py-2 text-sm font-mono font-bold text-blue-400">
              {{ msg.idHex }}
            </td>
            <td class="px-4 py-2 text-sm" :class="getEcuColor(msg.ecuName)">
              {{ msg.ecuName }}
            </td>
            <td class="px-4 py-2 text-sm text-gray-300">
              {{ msg.dlc }}
            </td>
            <td class="px-4 py-2 text-sm font-mono text-gray-300">
              {{ msg.dataHex }}
            </td>
            <td class="px-4 py-2 text-sm text-gray-400">
              {{ msg.signalType }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <footer class="mt-8 text-center text-gray-500 text-sm">
      CAN Bus Analyzer v1.0.0
    </footer>
  </div>
</template>
