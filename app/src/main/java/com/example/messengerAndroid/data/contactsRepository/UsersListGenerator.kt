package com.example.messengerAndroid.data.contactsRepository

import com.example.messengerAndroid.data.contactsRepository.contactModel.User
import com.example.messengerAndroid.utils.UniqueIdGenerator.getUniqueId
import com.github.javafaker.Faker

class UsersListGenerator {

    private var users = mutableListOf<User>()

    init {
        val faker = Faker.instance()
        users = (1..20).map {
            User(
            id = getUniqueId(),
            name = faker.name().name(),
            job = faker.company().name(),
            photo = IMAGES[it % IMAGES.size]
            )
        }.toMutableList()
    }

    fun getUsers() : List<User> {
        return users.toList()
    }


    companion object {
        private val IMAGES = mutableListOf(
            "https://parade.com/.image/t_share/MTkwNTgxMzUwMjEwMjgzMzg4/star-wars-characters-grogu-baby-yoda.jpg",
            "https://whatnerd.com/wp-content/uploads/2021/10/coolest-star-wars-characters-ahsoka-tano-featured.jpg",
            "https://www.shortlist.com/media/imager/201910/40419-original.jpg",
            "https://images.bauerhosting.com/legacy/media/6091/2b86/6de1/116a/4500/5e22/ackbar.jpg?q=80&w=440",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMJdvaJBGR65DRsBF5Rx45nM93FnFLgD641w&usqp=CAU",
            "https://images.saymedia-content.com/.image/t_share/MTc0NDU3NzM0Mjc5NDcyNzc0/best-characters-in-star-wars-galaxy-of-heroes.jpg",
            "https://parade.com/.image/t_share/MTkwNTgxMzM3ODYxNTMwNzQ5/star-wars-timeline-leia-carrie-fisher.jpg",
            "https://parade.com/.image/ar_1:1%2Cc_fill%2Ccs_srgb%2Cfl_progressive%2Cq_auto:good%2Cw_1200/MTkwNTgxMzUwNDgxNzMzNTAw/star-wars-characters-darth-vader.jpg"
        )
    }
}