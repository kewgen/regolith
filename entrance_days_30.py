import pandas as pd
import logging
import sys
from datetime import date, timedelta, datetime
from gg_opensearch import OpenSearch
from time import sleep, time
from config import (DICT_PRICE, DICT_LINES, DONATION_LINES)
from gg_owncloud import OwnCloud
from gg_vault import Vault
from gg_rocket_chat import send_to_chat
from gg_analytics_utils import decorate_all_in_modules, track_exec_time
import psycopg2
import sqlalchemy as db


login = 'postgres'
password = 'YdafKid7sJ7djlOd'
db_name = 'analytics_2'

engine = db.create_engine(
    f"postgresql+psycopg2://{login}:{password}@localhost/{db_name}"
    )
conn = engine.connect()

"""
Аналитика 30-ти дневного входа
"""
logger = logging.getLogger("entrance_days_30")
aow_engine = Vault("analytics")
aow_engine.login()
OC_CREDENTIALS = aow_engine.get_secret('public/owncloud_python')
OC = OwnCloud(OC_CREDENTIALS['login'], OC_CREDENTIALS['password'])
OS = OpenSearch("AOW", "prod")
OC_PATH = '/entrance_days_30/'
CHANNEL = "#analytics-report"

def commit_sql_command(sql_query):
     connect = psycopg2.connect(host='localhost', user=login, password=password, dbname=db_name)
     cursor = connect.cursor()
     cursor.execute(sql_query)
     connect.commit()
     cursor.close()
     connect.close()


def from_os_to_sql(all_idx_lst, test=''):
     df_all_res = pd.DataFrame(columns=['ResName', 'Date', 'RankLine', 'DonLine'])
     df_all_prfls = pd.DataFrame()
     for key_don, don_range in DONATION_LINES.items():
          for key_rank_line, rank_range in DICT_LINES.items():
               print('In process, donation:', key_don, ', rank:', key_rank_line)
               
               dict_res = {
                         'hero_card_epic': f'''SELECT Profile, Source, HeroCardsCount FROM {', '.join(all_idx_lst)}
                                             WHERE HeroCardsType=3 AND InCome=true 
                                             AND EventType='HeroCard' AND Rank BETWEEN {rank_range[0]} 
                                             AND {rank_range[1]} {test} AND Donation BETWEEN {don_range[0]} 
                                             AND {don_range[1]} AND NOT Account IN (9350959, 11407394) 
                                             AND IsTester=false''',
                          'gold': f'''SELECT Profile, EventType, GoldChange FROM {', '.join(all_idx_lst)} 
                                      WHERE GoldChange>0 AND Rank BETWEEN {rank_range[0]} 
                                      AND {rank_range[1]} {test} 
                                      AND Donation BETWEEN {don_range[0]} AND {don_range[1]} 
                                      AND NOT Account IN (9350959, 11407394) 
                                      AND IsTester=false''',
                          'credit': f'''SELECT Profile, EventType, CreditsChange FROM {', '.join(all_idx_lst)}
                                        WHERE CreditsChange>0 AND Rank BETWEEN {rank_range[0]} 
                                        AND {rank_range[1]} {test} AND Donation BETWEEN {don_range[0]} 
                                        AND {don_range[1]} AND NOT Account IN (9350959, 11407394) 
                                        AND IsTester=false ''',
                         'tokens': f'''SELECT Profile, EventType, TokensChange FROM {', '.join(all_idx_lst)}
                                        WHERE TokensChange>0 AND Rank BETWEEN {rank_range[0]} 
                                        AND {rank_range[1]} {test} AND Donation BETWEEN {don_range[0]} 
                                        AND {don_range[1]} AND NOT Account IN (9350959, 11407394)
                                        AND IsTester=false''',
                          'speedup_1': f'''SELECT Profile, Source, 1 as count  FROM {', '.join(all_idx_lst)} 
                                           WHERE InCome=true AND EventType='UpgradeBuster' 
                                           AND Id=4 AND Rank BETWEEN {rank_range[0]} 
                                           AND {rank_range[1]} {test} AND Donation BETWEEN {don_range[0]} 
                                           AND {don_range[1]} AND NOT Account IN (9350959, 11407394) 
                                           AND IsTester=false''',
                          'speedup_3': f'''SELECT Profile, Source, 1 as count  FROM {', '.join(all_idx_lst)} 
                                              WHERE InCome=true AND EventType='UpgradeBuster' 
                                              AND Id=1 AND Rank BETWEEN {rank_range[0]} 
                                              AND {rank_range[1]} {test} AND Donation BETWEEN {don_range[0]} 
                                              AND {don_range[1]} AND NOT Account IN (9350959, 11407394) 
                                              AND IsTester=false''',
                          'speedup_7': f'''SELECT Profile, Source, 1 as count FROM {', '.join(all_idx_lst)} 
                                              WHERE InCome=true AND EventType='UpgradeBuster' 
                                              AND Id=2 AND Rank BETWEEN {rank_range[0]} 
                                              AND {rank_range[1]} {test} AND Donation BETWEEN {don_range[0]} 
                                              AND {don_range[1]} AND NOT Account IN (9350959, 11407394) 
                                              AND IsTester=false''',
                          'cards_common': f'''SELECT Profile, Source, CardsCount FROM {', '.join(all_idx_lst)}
                                              WHERE CardsType=1 AND InCome=true 
                                              AND EventType='SkillCards' AND Rank BETWEEN {rank_range[0]} 
                                              AND {rank_range[1]} {test} AND Donation BETWEEN {don_range[0]} 
                                              AND {don_range[1]} AND NOT Account IN (9350959, 11407394) 
                                              AND IsTester=false''',
                          'cards_rare': f'''SELECT Profile, Source, CardsCount FROM {', '.join(all_idx_lst)}
                                              WHERE CardsType=2 AND InCome=true 
                                              AND EventType='SkillCards' AND Rank BETWEEN {rank_range[0]} 
                                              AND {rank_range[1]} {test} AND Donation BETWEEN {don_range[0]} 
                                              AND {don_range[1]} AND NOT Account IN (9350959, 11407394) 
                                              AND IsTester=false''',
                          'cards_epic': f'''SELECT Profile, Source, CardsCount FROM {', '.join(all_idx_lst)}
                                              WHERE CardsType=3 AND InCome=true 
                                              AND EventType='SkillCards' AND Rank BETWEEN {rank_range[0]} 
                                              AND {rank_range[1]} {test} AND Donation BETWEEN {don_range[0]} 
                                              AND {don_range[1]} AND NOT Account IN (9350959, 11407394) 
                                              AND IsTester=false''',
                          'boosts_blue': f'''SELECT Profile, Source, 1 as count  FROM {', '.join(all_idx_lst)} 
                                              WHERE BoostType=0 AND InCome=true 
                                              AND EventType='Boosts' AND Rank BETWEEN {rank_range[0]} 
                                              AND {rank_range[1]} {test} AND Donation BETWEEN {don_range[0]} 
                                              AND {don_range[1]} AND NOT Account IN (9350959, 11407394) 
                                              AND IsTester=false''',
                          'boosts_red': f'''SELECT Profile, Source, 1 as count  FROM {', '.join(all_idx_lst)} 
                                              WHERE BoostType=1 AND InCome=true 
                                              AND EventType='Boosts' AND Rank BETWEEN {rank_range[0]} 
                                              AND {rank_range[1]} {test} AND Donation BETWEEN {don_range[0]} 
                                              AND {don_range[1]} AND NOT Account IN (9350959, 11407394) 
                                              AND IsTester=false''',
                          'boosts_gold': f'''SELECT Profile, Source, 1 as count FROM {', '.join(all_idx_lst)} 
                                              WHERE BoostType=2 AND InCome=true 
                                              AND EventType='Boosts' AND Rank BETWEEN {rank_range[0]} 
                                              AND {rank_range[1]} {test} AND Donation BETWEEN {don_range[0]} 
                                              AND {don_range[1]} AND NOT Account IN (9350959, 11407394) 
                                              AND IsTester=false''',
                          'hero_card_common': f'''SELECT Profile, Source, HeroCardsCount FROM {', '.join(all_idx_lst)}
                                                   WHERE HeroCardsType=1 AND InCome=true 
                                                   AND EventType='HeroCard' AND Rank BETWEEN {rank_range[0]} 
                                                   AND {rank_range[1]} {test} AND Donation BETWEEN {don_range[0]} 
                                                   AND {don_range[1]} AND NOT Account IN (9350959, 11407394) 
                                                   AND IsTester=false''',
                         'hero_card_rare': f'''SELECT Profile, Source, HeroCardsCount FROM {', '.join(all_idx_lst)}
                                                  WHERE HeroCardsType=2 AND InCome=true 
                                                  AND EventType='HeroCard' AND Rank BETWEEN {rank_range[0]} 
                                                  AND {rank_range[1]} {test} AND Donation BETWEEN {don_range[0]} 
                                                  AND {don_range[1]} AND NOT Account IN (9350959, 11407394) 
                                                  AND IsTester=false'''
                         }

               for key in dict_res.keys():
                    print('start event:', key, datetime.now())
                    logger.debug(f"Start dowload {key} for "
                    f"rank between {rank_range[0]}  AND {rank_range[1]}" 
                    f"AND donation between {don_range[0]} AND {don_range[1]}") 
                    data_raw = OS.get_data_with_cursor(dict_res[key])
                    #sleep(10)

                    df_source = pd.DataFrame(data_raw, columns=['Profile', 'Event', key])
                    logger.debug(f"Download {key} complete")
                    df_source = df_source.replace({
                         "Tutorial": "TutorialBox", "Supply": "SupplyBox", "Purchase": "PurchaseBox",
                         "Milestone": "MilestoneBox", "Daily": "DailyBox", "ClanSupply": "ClanSupplyBox",
                         "SkillCardConversion": "ItemConversion", "BoostConvert": "ItemConversion",
                         "BattlePassBox": "BattlePassReward"})
                    df_sum_gr = (df_source.groupby('Event', as_index=False).agg({key:'sum'})
                                          .rename({key:'ResSum'}, axis=1))
                    df_sum_gr[['ResName', 'Date', 'RankLine', 'DonLine']] = (key, DT_YESTERDAY, key_rank_line, key_don)
                    df_all_res = pd.concat([df_all_res, df_sum_gr])
                    
                    
                    df_prof_list = df_source[['Profile', 'Event']].drop_duplicates(ignore_index=True)
                    df_prof_list[['ResName', 'RankLine', 'DonLine']] = (key, key_rank_line, key_don)
                    df_all_prfls = pd.concat([df_all_prfls, df_prof_list])
     df_all_res['ResSum'] = (df_all_res['ResSum']).astype('int')
     df_all_res.to_sql('res_sum_30d_current', engine, index=False, if_exists='replace')
     df_all_prfls.to_sql('profiles_30d_vert_current', engine, index=False, if_exists='replace')

     commit_sql_command(
          '''
          INSERT INTO profiles_30d_vert 
               SELECT * FROM profiles_30d_vert_current EXCEPT SELECT * FROM profiles_30d_vert
          ''')
     
     commit_sql_command(
          '''
          INSERT INTO res_sum_30d 
               SELECT * FROM res_sum_30d_current EXCEPT SELECT * FROM res_sum_30d
          ''')
     return


def save_main_tables_on_cloud():
     df_res_sum = pd.read_sql_query(
          f'''SELECT * 
               FROM res_sum_30d 
               WHERE date_part('month', "Date")={DT_YESTERDAY.month}''', engine)
     df_res_sum = pd.read_sql_query(
          f'''SELECT 
               "DonLine", "RankLine", "Event", 
               SUM(case when "ResName" = 'hero_card_epic' then "ResSum" end) as "hero_card_epic", 
               SUM(case when "ResName" = 'credit' then "ResSum" end) as "credit",
               SUM(case when "ResName" = 'speedup_1' then "ResSum" end) as "speedup_1",
               SUM(case when "ResName" = 'speedup_3' then "ResSum" end) as "speedup_3",
               SUM(case when "ResName" = 'speedup_7' then "ResSum" end) as "speedup_7",
               SUM(case when "ResName" = 'cards_common' then "ResSum" end) as "cards_common",
               SUM(case when "ResName" = 'cards_rare' then "ResSum" end) as "cards_rare",
               SUM(case when "ResName" = 'cards_epic' then "ResSum" end) as "cards_epic",
               SUM(case when "ResName" = 'boosts_blue' then "ResSum" end) as "boosts_blue",
               SUM(case when "ResName" = 'boosts_red' then "ResSum" end) as "boosts_red",
               SUM(case when "ResName" = 'boosts_gold' then "ResSum" end) as "boosts_gold",
               SUM(case when "ResName" = 're_roll' then "ResSum" end) as "re_roll",
               SUM(case when "ResName" = 'hero_card_common' then "ResSum" end) as "hero_card_common",
               SUM(case when "ResName" = 'hero_card_rare' then "ResSum" end) as "hero_card_rare"
               FROM res_sum_30d
               WHERE date_part('month', "Date")={DT_YESTERDAY.month}
               GROUP BY "DonLine", "RankLine", "Event" ''', engine)
     

     df_res_sum.set_index(["DonLine", "RankLine", "Event"], inplace=True)
     df_res_sum_prc = pd.DataFrame()
     for col in df_res_sum.columns:
          df_res_sum_prc[col] = (df_res_sum[col]/
                                   (df_res_sum.groupby(['DonLine', 'RankLine'])[col]
                                                       .transform('sum')))
     
     df_prfls = pd.read_sql_query(
          f'''
          SELECT 
               "DonLine", "RankLine", "Event", 
               COUNT(case when "ResName" = 'hero_card_epic' then "Profile" end) as "hero_card_epic", 
               COUNT(case when "ResName" = 'credit' then "Profile" end) as "credit",
               COUNT(case when "ResName" = 'speedup_1' then "Profile" end) as "speedup_1",
               COUNT(case when "ResName" = 'speedup_3' then "Profile" end) as "speedup_3",
               COUNT(case when "ResName" = 'speedup_7' then "Profile" end) as "speedup_7",
               COUNT(case when "ResName" = 'cards_common' then "Profile" end) as "cards_common",
               COUNT(case when "ResName" = 'cards_rare' then "Profile" end) as "cards_rare",
               COUNT(case when "ResName" = 'cards_epic' then "Profile" end) as "cards_epic",
               COUNT(case when "ResName" = 'boosts_blue' then "Profile" end) as "boosts_blue",
               COUNT(case when "ResName" = 'boosts_red' then "Profile" end) as "boosts_red",
               COUNT(case when "ResName" = 'boosts_gold' then "Profile" end) as "boosts_gold",
               COUNT(case when "ResName" = 're_roll' then "Profile" end) as "re_roll",
               COUNT(case when "ResName" = 'hero_card_common' then "Profile" end) as "hero_card_common",
               COUNT(case when "ResName" = 'hero_card_rare' then "Profile" end) as "hero_card_rare"
               FROM profiles_30d_vert
               GROUP BY "DonLine", "RankLine", "Event" ''', engine)
     
     df_prfls.set_index(["DonLine", "RankLine", "Event"], inplace=True)
     df_prfls = df_prfls.add_suffix('_prfls')

     df_abs = pd.merge(df_res_sum, df_prfls, left_index=True, right_index=True)
     for col in df_res_sum.columns:
          df_abs[col+'_by_prfls'] = df_abs[col] / df_abs[col+'_prfls']
     df_abs.sort_index(inplace=True, axis=1)


     logger.debug(df_abs.info())
     file_name = f"{DT_YESTERDAY.month}get_res_abs_test.csv"
     df_abs.to_csv(file_name)
     OC.put_file_to_cloud(OC_PATH + file_name)

     logger.debug(df_res_sum_prc.info())
     file_name = f"{DT_YESTERDAY.month}get_res_sum_test.csv"
     df_res_sum_prc.to_csv(file_name)
     OC.put_file_to_cloud(OC_PATH + file_name)

     # удаляю таблицу с аккаунтами за предыдущий месяц
     commit_sql_command(
          '''
          TRUNCATE profiles_30d_vert;
          DELETE FROM profiles_30d_vert;
          ''')
     



def percent_res_getting(): # % ресурсов в голде
    df_res = pd.read_sql_query(f'''
          SELECT    "RankLine",     
                    SUM(credit) AS credit, 
                    SUM(tokens) AS tokens, 
                    SUM(speedup_1) AS speedup_1, 
                    SUM(speedup_3) AS speedup_3, 
                    SUM(speedup_7) AS speedup_7, 
                    SUM(cards_common) AS cards_common, 
                    SUM(cards_rare) AS cards_rare, 
                    SUM(cards_epic) AS cards_epic,
                    SUM(boosts_blue) AS boosts_blue, 
                    SUM(boosts_red) AS boosts_red, 
                    SUM(boosts_gold) AS boosts_gold, 
                    SUM(re_roll) AS re_roll, 
                    SUM(hero_card_common) AS hero_card_common, 
                    SUM(hero_card_rare) AS hero_card_rare, 
                    SUM(hero_card_epic) AS hero_card_epic 
          FROM all_res_from_30d_entr
          WHERE date_part('month', "Date")={DT_YESTERDAY.month} 
               AND "for_ads"=false 
               AND "IsBuyBack"=false 
          GROUP BY "RankLine"
                               ''', engine)
    df_res.set_index('RankLine', inplace=True)
#     res.query("for_ads == False",inplace=True)
#     res.drop(columns=['for_ads'], inplace=True)
#     res.index = ['line_1', 'line_2', 'line_3']

    for resource, val_res in DICT_PRICE.items():
         df_res[resource] *= val_res

     
    #res = res.astype(float)
    percent_res = df_res.copy()
    
    for col in percent_res.columns:
        percent_res[col] = (df_res[col] / df_res.sum(axis=1)).round(3).fillna(0)

    file_name =  f'{DT_YESTERDAY.month}percent_res_test.csv'
    percent_res.to_csv(file_name)
    OC.put_file_to_cloud(OC_PATH + file_name)


def get_all_res_30d(rank_line, all_idx_lst, costgold=''):
     rank_range = DICT_LINES[rank_line]
     df_total = pd.DataFrame()
     for idx in all_idx_lst:
          logger.debug("Start download data from " + idx)
          query = f"SELECT ForAds, CreditsChange, TokensChange, SpeedUp1,\
                SpeedUp3, SpeedUp7, SkillCardsBlue, SkillCardsRed, SkillCardsPurple,\
                BoostsBlue, BoostsRed, BoostsGold, RerollKeysChange, HeroCardCommon, \
                HeroCardRare, HeroCardEpic FROM {idx} \
                WHERE EventType='Entrance30Item'  AND ScaleCategory={rank_range[2]} \
                {costgold}"
          data_raw = OS.get_data_with_cursor(query)
          logger.debug("Download data from " + idx + " complete")
          data_raw = (pd.DataFrame(data_raw,
                         columns=['for_ads', 'credit', 'tokens', 'speedup_1', 'speedup_3', 
                             'speedup_7', 'cards_common', 'cards_rare', 'cards_epic',
                             'boosts_blue', 'boosts_red', 'boosts_gold', 're_roll', 
                             'hero_card_common', 'hero_card_rare', 'hero_card_epic'])
                     .fillna(0).groupby('for_ads', as_index=False).sum())
          
          sleep(1)
          if costgold=='':
               query_gold = f"SELECT ForAds, GoldChange FROM {idx}\
                    WHERE EventType='Entrance30Item'  AND ScaleCategory={rank_range[2]} \
                    AND CostGold=0"
               data_gold = OS.get_data_with_cursor(query_gold)
               data_gold = (pd.DataFrame(data_gold, columns=['for_ads', 'gold'])
                         .fillna(0).groupby('for_ads', as_index=False).sum())
               data_raw = data_raw.merge(data_gold, on='for_ads', how='left')
          sleep(1)

          df_total = pd.concat([df_total, data_raw]) 

     df_total = df_total.groupby('for_ads', as_index=False).sum()
     df_total['RankLine'] = rank_line
     df_total['Date'] = DT_YESTERDAY

     if costgold=='AND CostGold>0':
          df_total['IsBuyBack'] = True
     else:
          df_total['IsBuyBack'] = False

     df_total.to_sql('all_res_from_30d_entr', engine, index=False, if_exists='append')
     logger.debug(df_total.info())
     return 


def percent_for_ads():
     df_res = pd.read_sql_query(f'''
     SELECT    "RankLine",
               for_ads,     
               SUM(credit) AS credit, 
               SUM(tokens) AS tokens, 
               SUM(speedup_1) AS speedup_1, 
               SUM(cards_common) AS cards_common, 
               SUM(cards_rare) AS cards_rare,
               SUM(boosts_red) AS boosts_red, 
               SUM(boosts_gold) AS boosts_gold, 
               SUM(re_roll) AS re_roll, 
               SUM(hero_card_common) AS hero_card_common, 
               SUM(hero_card_rare) AS hero_card_rare, 
               SUM(hero_card_epic) AS hero_card_epic 
     FROM all_res_from_30d_entr
     WHERE date_part('month', "Date")={DT_YESTERDAY.month} 
          AND "IsBuyBack"=false
     GROUP BY "RankLine", for_ads
                              ''', engine)
     
     sum_ = df_res.groupby('RankLine', as_index=False).sum()
     ads = df_res.query("for_ads == True").reset_index(drop=True)
     sum_ = sum_.drop(columns=['RankLine']).reset_index(drop=True)
     ads.drop(columns=['RankLine'], inplace=True)
     percent = ads.copy().loc[:]

     for _, row in ads.iterrows():
          row = (ads.loc[_,:] / sum_.loc[_,:]).fillna(0)
          percent.iloc[_, :] = row

     file_name = f'{DT_YESTERDAY.month}percent_for_ads_test.csv'
     percent.to_csv(file_name)
     OC.put_file_to_cloud(OC_PATH + file_name)


def box_item_open(rank_range, type, aoweventsadd_index_dates, ForAds):
     cond = ' AND ForAds=False' if ForAds else ' '
     query = (f"SELECT  Profile, 1 FROM {', '.join(aoweventsadd_index_dates)} "
              f"WHERE EventType='Entrance30{type}' "
              f"AND ScaleCategory={rank_range[2]} {cond}")
     df_box = OS.get_data_with_cursor(query)
     sleep(10)
     df_box = pd.DataFrame(df_box, columns=['profile', 'box_open'])
     df_box = df_box.groupby('profile',as_index=False)['box_open'].sum()
     df_box.rename({'index':'profile',0:'box_open'}, axis=1, inplace=True)
     df_box = df_box['box_open'].describe(percentiles=[0.25, 0.5, 0.75, 0.95])
     return df_box


def box_item_stat(aoweventsadd_index_dates, last_month_date):
     line_box = pd.DataFrame()
     line_item = pd.DataFrame()

     for rank_line in DICT_LINES.keys():
          line_current_box = box_item_open(DICT_LINES[rank_line], 'Box', aoweventsadd_index_dates, False)
          line_current_item = box_item_open(DICT_LINES[rank_line], 'Item', aoweventsadd_index_dates, True)
          line_box = pd.concat([line_box, line_current_box], axis=1)
          line_item = pd.concat([line_item, line_current_item], axis=1)

     line_box.columns = ['line_1','line_2','line_3']
     line_item.columns = ['line_1','line_2','line_3']
     line_box.to_csv(f'{last_month_date.month}box_stat.csv')
     OC.put_file_to_cloud(OC_PATH + f'{last_month_date.month}box_stat.csv')
     line_item.to_csv(f'{last_month_date.month}item_stat.csv')
     OC.put_file_to_cloud(OC_PATH + f'{last_month_date.month}item_stat.csv')


def buy_back():
     df_all = pd.read_sql_query(f'''
          SELECT    "RankLine",
                    "for_ads",
                    "IsBuyBack",                   
                    SUM(credit) AS credit, 
                    SUM(tokens) AS tokens, 
                    SUM(speedup_1) AS speedup_1, 
                    SUM(speedup_3) AS speedup_3, 
                    SUM(speedup_7) AS speedup_7, 
                    SUM(cards_common) AS cards_common, 
                    SUM(cards_rare) AS cards_rare, 
                    SUM(cards_epic) AS cards_epic,
                    SUM(boosts_blue) AS boosts_blue, 
                    SUM(boosts_red) AS boosts_red, 
                    SUM(boosts_gold) AS boosts_gold, 
                    SUM(re_roll) AS re_roll, 
                    SUM(hero_card_common) AS hero_card_common, 
                    SUM(hero_card_rare) AS hero_card_rare, 
                    SUM(hero_card_epic) AS hero_card_epic 
          FROM all_res_from_30d_entr
          WHERE date_part('month', "Date")={DT_YESTERDAY.month} 
          GROUP BY "RankLine", "for_ads", "IsBuyBack"
                               ''', engine)



     # buy_back = pd.DataFrame()
     # for rank_line in DICT_LINES.keys():
     #      buy_back_res_current = get_all_res_30d(DICT_LINES[rank_line], all_idx_lst, costgold='AND CostGold>0')
     #      buy_back = pd.concat([buy_back, buy_back_res_current], ignore_index=True)
     # buy_back.dropna(inplace=True,axis=1)

     df_not_ads = df_all.query("for_ads == False").set_index(['RankLine'])
     df_buy_back = df_all.query('IsBuyBack==True').set_index(['RankLine'])
     

     df_buy_back = df_buy_back.drop(columns=['for_ads', 'IsBuyBack'])#.reset_index(drop=True)
     df_not_ads = df_not_ads.drop(columns=['for_ads', 'IsBuyBack'])#.reset_index(drop=True)
     percent = buy_back.copy().loc[:]


     for _, row in buy_back.iterrows():
          row = (df_buy_back.loc[_,:] / df_not_ads.loc[_,:]).fillna(0)
          percent.iloc[_, :] = row

     file_name = f'{last_month_date.month}percent_buyback.csv'
     percent.to_csv(file_name) 
     #OC.put_file_to_cloud(OC_PATH + file_name)


def main(dt_yesterday):
     logger.info("Script start")
     main_module = sys.modules[__name__]
     decorate_all_in_modules(track_exec_time, (main_module))
     
     global DT_YESTERDAY
     DT_YESTERDAY = dt_yesterday

     all_idx_lst, aoweventsadd_index_dates = create_idx_lst()

     from_os_to_sql(all_idx_lst)

     if (DT_YESTERDAY+timedelta(1)).day==1:
          save_main_tables_on_cloud()

     # for rank_line in list(DICT_LINES.keys()):
     #      get_all_res_30d(rank_line, all_idx_lst)
     #      get_all_res_30d(rank_line, all_idx_lst, 'AND CostGold>0')

     # if (DT_YESTERDAY+timedelta(1)).day==1:
     #      percent_res_getting()
     #      percent_for_ads()
     # buy_back()
     # box_item_stat(aoweventsadd_index_dates, dt_yesterday)
     # send_to_chat("entrance_days_30 complete", CHANNEL)
     # logger.info("Script complete")


# def create_idx_lst(last_month_date:date):
#      date_start = date(last_month_date.year,last_month_date.month, 1)
#      date_end = last_month_date
#      index_dates = [(date_end - timedelta(days)).strftime(r'%Y.%m.%d') 
#                     for days in range((date_end - date_start).days + 1)]
#      aoweventsadd_index_dates = ["aoweventsadd-" + date_ for date_ in index_dates]
#      aowevents_index_dates = ["aowevents-" + date_ for date_ in index_dates]
#      all_idx_lst =  aowevents_index_dates + aoweventsadd_index_dates
#      return all_idx_lst, aoweventsadd_index_dates


def create_idx_lst():
     aoweventsadd_index_dates = ["aoweventsadd-" + DT_YESTERDAY.strftime(r'%Y.%m.%d') ]
     aowevents_index_dates = ["aowevents-" + DT_YESTERDAY.strftime(r'%Y.%m.%d') ]
     all_idx_lst =  aowevents_index_dates + aoweventsadd_index_dates
     return all_idx_lst, aoweventsadd_index_dates


#%%
if __name__ == "__main__":
     main(date.today()-timedelta(1))