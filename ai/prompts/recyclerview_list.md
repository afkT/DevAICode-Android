# RecyclerView & List (List Screens and Adapter)

This prompt defines how to implement list screens with RecyclerView, data binding, and updates. It applies to projects using MVVM, DataBinding, and ViewModel. If the project has a shared Adapter base or ItemBinding convention, follow that. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Adding or changing list screens (single or multi-type items), Adapter, ViewHolder, item layout, and data source. For projects using RecyclerView + DataBinding (or ViewBinding).

---

## Data source and ViewModel

- **Data** — List data lives in ViewModel (e.g. StateFlow&lt;List&lt;Item&gt;&gt; or LiveData); load via Repository or UseCase and expose as UI model.
- **Paging** — For large lists use Paging 3 or manual paging (offset/limit or cursor); in ViewModel handle load more, dedup, merge; avoid loading full list at once.
- **Empty / error / loading** — UiState includes list, loading, error, empty; show empty-state view when list is empty, distinct from error.

---

## Adapter and ViewHolder

- **Base** — If the project has a shared Adapter base (e.g. AdapterModel), extend it and follow its ItemBinding/multi-type rules. Otherwise use **ListAdapter** + **DiffUtil.ItemCallback** for incremental updates; avoid notifyDataSetChanged().
- **Stable IDs** — If items have a unique stable id, override getItemId() and setHasStableIds(true) in the constructor for better animations and DiffUtil.
- **ViewHolder** — One ViewHolder per item type; in onBindViewHolder only bind data and click callbacks; no network/DB access. With DataBinding set the binding variable (e.g. BR.item) per project convention.
- **Clicks and events** — Item click, long-press, child click go to Fragment/ViewModel via interface or lambda; with DataBinding use BindingAdapter or variable for lambda.

---

## Item layout and DataBinding

- **Layout** — With &lt;layout&gt; + &lt;data&gt;, variable is usually `item` (or project `itemValue`) and optional `listener`/`viewModel`; match BR.item etc. in Adapter.
- **Reuse** — Same structure = same layout and ViewHolder; multi-type: one layout and ViewHolder per type, dispatch in Adapter by getItemViewType.
- **Performance** — Keep item layout flat; avoid deep nesting or RecyclerView inside item; bind images with Glide etc. and lifecycle.

---

## DiffUtil and updates

- **ListAdapter** — Use ListAdapter&lt;Item, ViewHolder&gt; and implement DiffUtil.ItemCallback&lt;Item&gt;; submitList(newList) and let the system compute diff and update.
- **Equality** — For data class items, implement equals/hashCode (or use all fields). If only id equality matters, compare only id in DiffUtil; use content comparison for payload if needed.
- **Payload** — For partial update (e.g. selection) return payload from DiffUtil getChangePayload and handle in onBindViewHolder three-arg override.

---

## Screen structure

- **Fragment/Activity** — RecyclerView in layout; bind or observe ViewModel list and call adapter.submitList. Trigger first load and pull-to-refresh from ViewModel; Adapter does not hold the data source, only receives via submitList.
- **Refresh / load more** — If using SmartRefreshLayout etc., pull-to-refresh calls ViewModel refresh, load more calls loadMore; ViewModel manages page/cursor and dedup.
