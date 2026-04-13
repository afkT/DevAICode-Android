# Paging 3 (Paged Loading)

This prompt defines how to implement paged lists with Paging 3. It applies to projects using MVVM, Room, RecyclerView, and Flow. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Paged list data from Room or network (or both); Paging 3 PagingSource/RemoteMediator, PagingData, Flow, and UI collect.

---

## Dependencies and basics

- **Dependency** — androidx.paging:paging-runtime (match project version). With Kotlin use Flow&lt;PagingData&gt; (kotlinx-coroutines). For Rx use paging-rxjava3 if needed.
- **Layers** — **PagingSource** (load one page from one source), **Pager** (pageSize, initialLoadSize, PagingSource or RemoteMediator), **UI** (Adapter with **AsyncPagingDataDiffer** or **PagingDataAdapter** submitting PagingData).

---

## PagingSource (single source)

- **Room** — DAO returns PagingSource&lt;Int, Entity&gt; (e.g. @Query("SELECT * FROM tbl") fun pagingSource(): PagingSource&lt;Int, Entity&gt;). Room implements PagingSource; key is Int (offset/position), value is entity.
- **Network** — Implement PagingSource&lt;Key, Value&gt;; in load(params: LoadParams&lt;Key&gt;) use params.key to request that page; return LoadResult.Page(data, prevKey, nextKey) or LoadResult.Error(e). Key is usually page number or cursor.
- **Refresh** — PagingSource should support invalidation and reload; trigger new Pager or re-collect Pager.flow in Repository or ViewModel on refresh.

---

## Pager and Flow

- **Pager** — Pager(config, pagingSourceFactory) or Pager(config) { pagingSource }. In config set pageSize, initialLoadSize, enablePlaceholders (optional). With Room, pagingSourceFactory returns dao.pagingSource().
- **Flow** — Pager(...).flow gives Flow&lt;PagingData&lt;Value&gt;&gt;. In ViewModel use cachedIn(viewModelScope) to avoid reload on config change; expose Flow&lt;PagingData&lt;Item&gt;&gt; or convert to StateFlow/LiveData.
- **RemoteMediator** (network + local) — When data is “network + Room cache” use **RemoteMediator**; in load(loadType, state) handle REFRESH/PREPEND/APPEND (fetch, write to Room), return MediatorResult.Success(endOfPaginationReached = ...) or MediatorResult.Error(e). Pager’s pagingSourceFactory gets PagingSource from Room and takes remoteMediator.

---

## UI layer

- **Adapter** — Use **PagingDataAdapter** (with DiffUtil.ItemCallback) or **AsyncPagingDataDiffer**. In Fragment/Activity: viewLifecycleOwner.lifecycleScope.launch { viewModel.pagingDataFlow.collectLatest { adapter.submitData(it) } } (or collect then submitData).
- **Load state** — Use PagingDataAdapter.loadStateFlow or LoadStateCollector for refresh/append/prepend (Loading/NotLoading/Error) to show loading, error, or “no more”. Optional **LoadStateAdapter** at head/foot for loading/error.
- **Refresh** — adapter.refresh() invalidates PagingSource and reloads; align with ViewModel refresh (e.g. pull-to-refresh triggers ViewModel refresh then adapter.refresh(), or only adapter.refresh() if source is already updated).

---

## ViewModel and Repository

- **Repository** — If Room only, expose Flow&lt;PagingData&lt;Entity&gt;&gt;, e.g. Pager(config) { dao.pagingSource() }.flow. With RemoteMediator, Repository or DI provides RemoteMediator and DAO; build Pager in ViewModel or Repository.
- **ViewModel** — Expose Flow&lt;PagingData&lt;UiModel&gt;&gt; (map Entity to UiModel in PagingSource chain or PagingData.map). cachedIn(viewModelScope) avoids duplicate subscription and reload.
- **Boundary** — Domain layer does not depend on Paging types; expose Flow&lt;PagingData&lt;DomainModel&gt;&gt; from data or map PagingData&lt;Entity&gt; to UiModel in UI.

---

## Notes

- **pageSize** — Match or slightly exceed backend page size to avoid too many requests; initialLoadSize can be a bit larger to reduce first-screen requests.
- **Thread** — Paging uses Dispatchers.IO internally; Room PagingSource is already correct; in custom PagingSource use suspend or callbacks for network, do not block main thread.
- **Testing** — Unit-test PagingSource load input/output; use PagingData.from(list) for Adapter/UI or snapshot tests.
