package com.bootProject.repository.cart;

import com.bootProject.dto.CartDTO;
import com.bootProject.entity.Cart;
import com.bootProject.entity.Member;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;
import static com.bootProject.entity.QCart.cart;
import static com.bootProject.entity.QMember.member;
import static com.bootProject.entity.QItem.item;
import static com.bootProject.entity.QSaveFile.saveFile;

@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public List<CartDTO> selectCartList(String email) {
        List<CartDTO> cartList = new ArrayList<CartDTO>();
        cartList = queryFactory
                .select(
                        Projections.constructor(
                                CartDTO.class, cart.id, cart.quantity,
                                item.title,item.contents, item.price, item.category,
                                saveFile.storedFileName
                        )
                )
                .from(cart)
                .innerJoin(cart.memberId, member)
                .innerJoin(cart.itemId, item)
                .innerJoin(item.fileList, saveFile)
                .where(member.email.eq(email))
                .fetch();
        return cartList;
    }

    @Override
    public Cart selectByItemIdAndMemberId(Long itemId, Long memberId) {
        return queryFactory
                .select(cart)
                .from(cart)
                .innerJoin(cart.itemId, item).fetchJoin()
                .innerJoin(cart.memberId, member).fetchJoin()
                .where(
                    member.id.eq(memberId).and(item.id.eq(itemId))
                ).fetchOne();
    }

    @Override
    public Page<CartDTO> selectCartPage(Pageable pageable, String email) {
        List<CartDTO> cartList = new ArrayList<CartDTO>();
        cartList = queryFactory
                .select(
                        Projections.constructor(
                                CartDTO.class, cart.id, cart.quantity,
                                item.title,item.contents, item.price, item.category,
                                saveFile.storedFileName
                        )
                )
                .from(cart)
                .innerJoin(cart.memberId, member)
                .innerJoin(cart.itemId, item)
                .innerJoin(item.fileList, saveFile)
                .where(member.email.eq(email))
                .orderBy(cart.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(cart.count())
                .from(cart)
                .innerJoin(cart.memberId, member)
                .innerJoin(cart.itemId, item)
                .innerJoin(item.fileList, saveFile)
                .where(member.email.eq(email));

        return PageableExecutionUtils.getPage(cartList, pageable, count::fetchOne);
    }
}
