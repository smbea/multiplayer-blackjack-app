import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.blackjack.R


internal class CardsAdapter(private var cardsList: ArrayList<String>, private var mine:Boolean) :

    RecyclerView.Adapter<CardsAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.image)
    }

    private lateinit var context:Context

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView : View

        if (mine){
            itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cards_list, parent, false)
        }else {
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.opponent_cards_list, parent, false)
        }
        context=parent.context
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val card = cardsList[position]
        //val cardImage = something
        holder.image.setImageResource(R.drawable.c2)
    }
    override fun getItemCount(): Int {
        return cardsList.size
    }

}
