import androidx.recyclerview.widget.DiffUtil
import com.country.information.common.RowResponse
import org.jetbrains.annotations.Nullable

class CountryDetailsDiffCallback(
    private val oldList: List<RowResponse>,
    private val newList: List<RowResponse>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].title == newList[newItemPosition].title

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        val (_, value, name) = oldList[oldPosition]
        val (_, value1, name1) = newList[newPosition]

        return name == name1 && value == value1
    }
}